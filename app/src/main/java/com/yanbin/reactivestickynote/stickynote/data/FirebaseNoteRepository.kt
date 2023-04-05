package com.yanbin.reactivestickynote.stickynote.data

import com.google.firebase.firestore.*
import com.yanbin.common.YBSize
import com.yanbin.reactivestickynote.account.Account
import com.yanbin.reactivestickynote.stickynote.model.*
import com.yanbin.utils.toIO
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject
import java.util.*
import java.util.concurrent.TimeUnit

class FirebaseNoteRepository : NoteRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val allNoteIdsSubject = BehaviorSubject.create<List<String>>()
    private val allSelectedNotesSubject = BehaviorSubject.create<Map<String, SelectedNote>>()
    private val updatingNoteSubject = BehaviorSubject.createDefault(Optional.empty<StickyNote>())

    private val registrations = hashMapOf<String, ListenerRegistration>()
    private val noteSubjects = hashMapOf<String, Subject<StickyNote>>()

    private val noteQuery = firestore.collection(COLLECTION_NOTES)
        .limit(100)

    private val selectNoteQuery = firestore.collection(COLLECTION_SELECTED_NOTES)
        .limit(100)

    init {
        noteQuery.addSnapshotListener { result, e ->
            result?.let { onNoteUpdated(it) }
        }

        selectNoteQuery.addSnapshotListener { result, e ->
            result?.let { onSelectedNoteUpdated(it) }
        }

        updatingNoteSubject
            .throttleLast(1000, TimeUnit.MILLISECONDS)
            .toIO()
            .subscribe { optNote ->
                optNote.ifPresent { setNoteDocument(it) }
            }

        updatingNoteSubject
            .filter { it.isPresent }
            .debounce(1000, TimeUnit.MILLISECONDS)
            .subscribe {
                updatingNoteSubject.onNext(Optional.empty<StickyNote>())
            }
    }

    override fun getAllVisibleNoteIds(): Observable<List<String>> {
        return allNoteIdsSubject.hide()
    }

    override fun getNoteById(id: String): Observable<StickyNote> {
        if (noteSubjects[id] != null) {
            return combineNoteSignals(noteSubjects[id]!!, id)
        }

        val documentRef = createDocumentRef(id)
        val noteSubject = BehaviorSubject.create<StickyNote>()
        val registration = documentRef.addSnapshotListener { snapshot, error ->
            if (snapshot != null) {
                val note = documentToNotes(snapshot) ?: return@addSnapshotListener
                noteSubject.onNext(note)
            }
        }

        registrations[id] = registration
        noteSubjects[id] = noteSubject

        return combineNoteSignals(noteSubject, id)
    }

    private fun combineNoteSignals(remote: Observable<StickyNote>, id: String): Observable<StickyNote> {
        return updatingNoteSubject.switchMap { optNote ->
            if (optNote.isPresent && optNote.get().id == id) {
                remote.map { optNote.get() }
            } else {
                remote
            }
        }
    }

    override fun createNote(note: StickyNote) {
        setNoteDocument(note)
    }

    override fun putNote(stickyNote: StickyNote) {
        updatingNoteSubject.onNext(Optional.of(stickyNote))
    }

    override fun updateNote(noteId: String, attributes: List<NoteAttribute>) {
        TODO("Not yet implemented")
    }

    override fun deleteNote(noteId: String) {
        registrations[noteId]?.remove()
        noteSubjects[noteId]?.onComplete()

        firestore.collection(COLLECTION_NOTES)
            .document(noteId)
            .delete()
    }

    override fun getAllSelectedNotes(): Observable<List<SelectedNote>> {
        return allSelectedNotesSubject.hide()
            .map { it.values.toMutableList() }
    }

    override fun setNoteSelection(noteId: String, account: Account) {
        val selectedNoteData = hashMapOf(
            FIELD_NOTE_ID to noteId,
            FIELD_USER_NAME to account.userName
        )

        firestore.collection(COLLECTION_SELECTED_NOTES)
            .document(account.id)
            .set(selectedNoteData)
    }

    override fun removeNoteSelection(noteId: String, account: Account) {
        runCatching {
            firestore.collection(COLLECTION_SELECTED_NOTES)
                .document(account.id)
                .delete()
        }
    }

    private fun createDocumentRef(id: String): DocumentReference {
        return firestore.collection(COLLECTION_NOTES).document(id)
    }

    private fun onNoteUpdated(snapshot: QuerySnapshot) {
        if (snapshot.documentChanges.any { it.type == DocumentChange.Type.ADDED || it.type == DocumentChange.Type.REMOVED }) {
            val allNoteIds = snapshot.map { it.id }
            allNoteIdsSubject.onNext(allNoteIds)
        }

        snapshot.documentChanges
            .filter { it.type == DocumentChange.Type.REMOVED }
            .forEach { documentChange ->
                val id = documentChange.document.id
                registrations[id]?.remove()
                noteSubjects[id]?.onComplete()
            }
    }

    private fun onSelectedNoteUpdated(snapshot: QuerySnapshot) {
        val allSelectedNotes = snapshot.documents
            .mapNotNull { document ->
                val id = document.id
                val data: Map<String, Any> = document.data as? Map<String, Any> ?: return@mapNotNull null

                val userName = data[FIELD_USER_NAME] as? String ?: return@mapNotNull null
                val noteId = data[FIELD_NOTE_ID] as? String ?: return@mapNotNull null

                id to SelectedNote(noteId, userName)
            }
            .toMap()

        allSelectedNotesSubject.onNext(allSelectedNotes)
    }

    private fun setNoteDocument(stickyNote: StickyNote) {
        val noteData = hashMapOf(
            FIELD_TEXT to stickyNote.text,
            FIELD_COLOR to stickyNote.color.color,
            FIELD_POSITION_X to stickyNote.position.x,
            FIELD_POSITION_Y to stickyNote.position.y,
            FIELD_SIZE_WIDTH to stickyNote.size.width,
            FIELD_SIZE_HEIGHT to stickyNote.size.height,
        )

        firestore.collection(COLLECTION_NOTES)
            .document(stickyNote.id)
            .set(noteData)
    }

    private fun documentToNotes(document: DocumentSnapshot?): StickyNote? {
        val data: Map<String, Any> = document?.data as? Map<String, Any> ?: return null
        val text = data[FIELD_TEXT] as? String ?: ""
        val color = YBColor(data[FIELD_COLOR] as? Long ?: 0L)
        val positionX = data[FIELD_POSITION_X] as? Double ?: 0f
        val positionY = data[FIELD_POSITION_Y] as? Double ?: 0f
        val width = data[FIELD_SIZE_WIDTH] as? Double ?: StickyNote.DEFAULT_SIZE
        val height = data[FIELD_SIZE_HEIGHT] as? Double ?: StickyNote.DEFAULT_SIZE
        val position = Position(positionX.toFloat(), positionY.toFloat())
        val size = YBSize(width.toFloat(), height.toFloat())
        return StickyNote(document.id, text, position, size, color)
    }

    companion object {
        const val COLLECTION_NOTES = "Notes"
        const val COLLECTION_SELECTED_NOTES = "SelectedNotes"
        const val FIELD_TEXT = "text"
        const val FIELD_COLOR = "color"
        const val FIELD_POSITION_X = "positionX"
        const val FIELD_POSITION_Y = "positionY"
        const val FIELD_SIZE_WIDTH = "sizeWidth"
        const val FIELD_SIZE_HEIGHT = "sizeHeight"
        const val FIELD_USER_NAME = "userName"
        const val FIELD_NOTE_ID = "noteId"
    }
}