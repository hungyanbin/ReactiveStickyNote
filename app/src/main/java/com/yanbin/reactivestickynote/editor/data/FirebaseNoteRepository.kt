package com.yanbin.reactivestickynote.editor.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.*
import com.yanbin.reactivestickynote.editor.model.StickyNote
import com.yanbin.reactivestickynote.editor.model.Position
import com.yanbin.reactivestickynote.editor.model.YBColor
import com.yanbin.utils.toIO
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject
import java.util.*
import java.util.concurrent.TimeUnit

class FirebaseNoteRepository : NoteRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val allNoteIdsSubject = BehaviorSubject.create<List<String>>()
    private val updatingNoteSubject = BehaviorSubject.createDefault(Optional.empty<StickyNote>())

    private val registrations = hashMapOf<String, ListenerRegistration>()
    private val noteSubjects = hashMapOf<String, Subject<StickyNote>>()

    private val query = firestore.collection(COLLECTION_NOTES)
        .limit(100)

    init {
        query.addSnapshotListener { result, e ->
            result?.let { onSnapshotUpdated(it) }
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

    override fun deleteNote(noteId: String) {
        registrations[noteId]?.remove()
        noteSubjects[noteId]?.onComplete()

        firestore.collection(COLLECTION_NOTES)
            .document(noteId)
            .delete()
    }

    private fun createDocumentRef(id: String): DocumentReference {
        return firestore.collection(COLLECTION_NOTES).document(id)
    }

    private fun onSnapshotUpdated(snapshot: QuerySnapshot) {
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

    private fun setNoteDocument(stickyNote: StickyNote) {
        val noteData = hashMapOf(
            FIELD_TEXT to stickyNote.text,
            FIELD_COLOR to stickyNote.color.color,
            FIELD_POSITION_X to stickyNote.position.x,
            FIELD_POSITION_Y to stickyNote.position.y
        )

        firestore.collection(COLLECTION_NOTES)
            .document(stickyNote.id)
            .set(noteData)
    }

    private fun documentToNotes(document: DocumentSnapshot?): StickyNote? {
        val data: Map<String, Any> = document?.data as? Map<String, Any> ?: return null
        val text = data[FIELD_TEXT] as String
        val color = YBColor(data[FIELD_COLOR] as Long)
        val positionX = data[FIELD_POSITION_X] as Double? ?: 0f
        val positionY = data[FIELD_POSITION_Y] as Double? ?: 0f
        val position = Position(positionX.toFloat(), positionY.toFloat())
        return StickyNote(document.id, text, position, color)
    }

    companion object {
        const val COLLECTION_NOTES = "Notes"
        const val FIELD_TEXT = "text"
        const val FIELD_COLOR = "color"
        const val FIELD_POSITION_X = "positionX"
        const val FIELD_POSITION_Y = "positionY"
    }
}