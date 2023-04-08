package com.yanbin.reactivestickynote.stickynote.data

import android.util.Log
import com.yanbin.reactivestickynote.account.Account
import com.yanbin.reactivestickynote.stickynote.model.NoteAttribute
import com.yanbin.reactivestickynote.stickynote.model.SelectedNote
import com.yanbin.reactivestickynote.stickynote.model.StickyNote
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.*
import io.ktor.util.reflect.*
import io.ktor.websocket.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.*
import kotlinx.serialization.protobuf.ProtoBuf
import java.util.*

class LocalHostNoteRepository: OldNoteRepository {

    private val allNotesSubject = BehaviorSubject.create<List<StickyNote>>()
    private val selectedNotesSubject = BehaviorSubject.createDefault(listOf(SelectedNote("", "")))
    private var clientWebSocketSession: DefaultClientWebSocketSession? = null
    // TODO refactor to use scope in ViewModel
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val logIncomingMessage = true

    init {
        val client = HttpClient {
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(ProtoBuf)
            }
        }
        val hostEmulator = "10.0.2.2"
        val hostDevice = "192.168.1.102"

        coroutineScope.launch(Dispatchers.IO) {
            client.webSocket(
                method = HttpMethod.Get,
                host = hostEmulator,
                port = 8081,
                path = "/notes"
            ) {
                clientWebSocketSession = this
                val outputRoutine = launch { handleOutputMessages() }
                val queryRoutine = launch { sendQueryMessage() }
                outputRoutine.join()
                queryRoutine.cancelAndJoin()
            }
            client.close()
        }
    }

    private suspend fun DefaultClientWebSocketSession.sendQueryMessage() {
        try {
            val message = SocketMessage.query()
            sendSerialized(message)
        } catch (e: Exception) {
            Log.e(TAG, "Error while sending query message ", e)
        }
    }

    private suspend fun DefaultClientWebSocketSession.handleOutputMessages() {
        try {
            for (message in incoming) {
                when (message) {
                    is Frame.Binary -> {
                        serializeMessage(message)
                    }
                    is Frame.Text -> {
                        Log.i(TAG, "Message from server: ${message.readText()} ")
                    }
                    else -> {}
                }
                message as? Frame.Text ?: continue

            }
        } catch (e: Exception) {
            Log.e(TAG, "Error while receiving: ", e)
        }
    }

    private suspend fun DefaultClientWebSocketSession.serializeMessage(message: Frame): Result<Unit> {
        return runCatching {
            tryToSerialize<FrameContract>(message,
                converter as KotlinxWebsocketSerializationConverter
            )?.let { frame: FrameContract ->
                if (logIncomingMessage) Log.i(TAG, "Message from server: $frame ")
                if (frame.isValid()) {
                    when (frame.type) {
                        FrameContract.Type.AllNotes -> updateAllNotes(frame.notes)
                        FrameContract.Type.UpdateNote -> updateLocalNoteFromServer(frame)
                        FrameContract.Type.Create -> newNoteCreatedFromServer(frame)
                        FrameContract.Type.Delete -> deleteNoteFromServer(frame)
                    }
                }
            }
        }
    }

    private fun deleteNoteFromServer(frame: FrameContract) {
        val currentNotes = allNotesSubject.value?.toMutableList() ?: throw IllegalStateException("No notes")
        val updatedNoteAttributes = frame.updatedNoteAttributes ?: throw IllegalStateException("Missing updatedNoteAttributes")
        val deletedId = updatedNoteAttributes.objectId
        currentNotes.removeIf { it.id == deletedId }

        allNotesSubject.onNext(currentNotes)
    }

    private fun updateAllNotes(notes: List<NoteDto>) {
        allNotesSubject.onNext(notes.map { it.toStickyNote() })
    }

    private fun updateLocalNoteFromServer(frame: FrameContract) {
        val updatedNoteAttributes = frame.updatedNoteAttributes ?: throw IllegalStateException("Missing updatedNoteAttributes")
        val currentNotes = allNotesSubject.value?.toMutableList() ?: throw IllegalStateException("No notes")
        val updatedIndex = currentNotes.indexOfFirst { it.id == updatedNoteAttributes.objectId }
        val selectionId = selectedNotesSubject.value?.first()?.noteId ?: ""
        if (selectionId == updatedNoteAttributes.objectId) throw IllegalStateException("No selection")

        currentNotes[updatedIndex] = updatedNoteAttributes.updateNote(currentNotes[updatedIndex])
        allNotesSubject.onNext(currentNotes)
    }

    private fun newNoteCreatedFromServer(frame: FrameContract) {
        val updatedNoteAttributes = frame.updatedNoteAttributes ?: throw IllegalStateException("Missing updatedNoteAttributes")
        val currentNotes = allNotesSubject.value?.toMutableList() ?: throw IllegalStateException("No notes")
        val newNote = updatedNoteAttributes.toStickyNote()

        currentNotes.add(newNote)
        allNotesSubject.onNext(currentNotes)
    }

    private suspend inline fun <reified T> DefaultClientWebSocketSession.tryToSerialize(frame: Frame, converter: KotlinxWebsocketSerializationConverter): T? {
        if (!converter.isApplicable(frame)) {
            throw WebsocketDeserializeException(
                "Converter doesn't support frame type ${frame.frameType.name}",
                frame = frame
            )
        }

        val typeInfo = typeInfo<T>()
        val result = converter.deserialize(
            charset = call.request.headers.suitableCharset(),
            typeInfo = typeInfo,
            content = frame
        )

        if (result is T) return result
        if (result == null) {
            if (typeInfo.kotlinType?.isMarkedNullable == true) return null
            throw WebsocketDeserializeException("Frame has null content", frame = frame)
        }

        throw WebsocketDeserializeException(
            "Can't deserialize value : expected value of type ${T::class.simpleName}," +
                " got ${result::class.simpleName}",
            frame = frame
        )
    }

    override fun getAllVisibleNoteIds(): Observable<List<String>> {
        return allNotesSubject.map { notes -> notes.map { it.id } }
    }

    override fun getAllSelectedNotes(): Observable<List<SelectedNote>> {
        return selectedNotesSubject.hide()
    }

    override fun getNoteById(id: String): Observable<StickyNote> {
        return allNotesSubject.mapOptional { notes ->
            Optional.ofNullable(notes.find { it.id == id })
        }
    }

    override fun updateNote(noteId: String, attributes: List<NoteAttribute>) {
        selectedNotesSubject.onNext(
            listOf(SelectedNote(noteId, "Yachu"))
        )

        val updatedNoteAttributes = UpdatedNoteAttributes.fromAttributes(noteId, attributes)
        coroutineScope.launch(Dispatchers.IO) {
            clientWebSocketSession?.sendSerialized(
                SocketMessage.update(updatedNoteAttributes)
            )
        }
        val currentNotes = allNotesSubject.value?.toMutableList() ?: return
        val updatedIndex = currentNotes.indexOfFirst { it.id == noteId }
        currentNotes[updatedIndex] = updatedNoteAttributes.updateNote(currentNotes[updatedIndex])
        allNotesSubject.onNext(currentNotes)
    }

    override fun createNote(stickyNote: StickyNote) {
        coroutineScope.launch(Dispatchers.IO) {
            clientWebSocketSession?.sendSerialized(
                SocketMessage.create(UpdatedNoteAttributes.fromStickyNote(stickyNote))
            )
        }
    }

    override fun deleteNote(noteId: String) {
        coroutineScope.launch(Dispatchers.IO) {
            clientWebSocketSession?.sendSerialized(
                SocketMessage.delete(noteId)
            )
        }
    }

    override fun setNoteSelection(noteId: String, account: Account) {
        selectedNotesSubject.onNext(
            listOf(SelectedNote(noteId, account.userName))
        )
    }

    override fun removeNoteSelection(noteId: String, account: Account) {
        selectedNotesSubject.onNext(emptyList())
    }

    companion object {
        const val TAG = "LocalHostNoteRepository"
    }
}