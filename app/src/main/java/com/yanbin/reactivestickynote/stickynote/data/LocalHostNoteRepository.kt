package com.yanbin.reactivestickynote.stickynote.data

import android.util.Log
import com.yanbin.reactivestickynote.account.Account
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.protobuf.ProtoBuf
import java.util.Optional

class LocalHostNoteRepository: NoteRepository {

    private val allNotesSubject = BehaviorSubject.create<List<StickyNote>>()

    init {
        val client = HttpClient {
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(ProtoBuf)
            }
        }
        // TODO check scope
        GlobalScope.launch(Dispatchers.IO) {
            client.webSocket(
                method = HttpMethod.Get,
                host = "10.0.2.2",
                port = 8081,
                path = "/notes"
            ) {
                val outputRoutine = launch { handleOutputMessages() }
                outputRoutine.join()
            }
            client.close()
        }
    }

    private suspend fun DefaultClientWebSocketSession.handleOutputMessages() {
        try {
            for (message in incoming) {
                when (message) {
                    is Frame.Binary -> {
                        val notes = tryToSerialize<List<NoteDto>>(message,
                            converter as KotlinxWebsocketSerializationConverter
                        )!!
                        allNotesSubject.onNext(notes.map { it.toStickyNote() })
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
        return Observable.just(emptyList())
    }

    override fun getNoteById(id: String): Observable<StickyNote> {
        return allNotesSubject.mapOptional { notes ->
            Optional.ofNullable(notes.find { it.id == id })
        }
    }

    override fun putNote(stickyNote: StickyNote) {

    }

    override fun createNote(stickyNote: StickyNote) {

    }

    override fun deleteNote(noteId: String) {

    }

    override fun setNoteSelection(noteId: String, account: Account) {

    }

    override fun removeNoteSelection(noteId: String, account: Account) {

    }

    companion object {
        const val TAG = "LocalHostNoteRepository"
    }
}