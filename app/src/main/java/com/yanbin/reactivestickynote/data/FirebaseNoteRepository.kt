package com.yanbin.reactivestickynote.data

import com.google.firebase.firestore.*
import com.yanbin.reactivestickynote.model.Note
import com.yanbin.reactivestickynote.model.Position
import com.yanbin.reactivestickynote.model.YBColor
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject
import java.util.*
import java.util.concurrent.TimeUnit

class FirebaseNoteRepository(
    firebaseFacade: FirebaseFacade
): NoteRepository {
    private val firestore = firebaseFacade.getFirestore()
    private val allNoteIdsSubject = BehaviorSubject.create<List<String>>()
    private val updatingNoteSubject = BehaviorSubject.createDefault(Optional.empty<Note>())

    private val registrations = hashMapOf<String, ListenerRegistration>()
    private val noteSubjects = hashMapOf<String, Subject<Note>>()

    private val query = firestore.collection(COLLECTION_NOTES)
        .limit(100)

    init {
        query.addSnapshotListener { result, e ->
            result?.let { onSnapshotUpdated(it) }
        }

        updatingNoteSubject
            .throttleLast(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { optNote ->
                optNote.ifPresent { setNoteDocument(it) }
            }

        updatingNoteSubject
            .filter { it.isPresent }
            .debounce(300, TimeUnit.MILLISECONDS)
            .subscribe {
                updatingNoteSubject.onNext(Optional.empty<Note>())
            }
    }

    override fun getAllVisibleNoteIds(): Observable<List<String>> {
        return allNoteIdsSubject.hide()
    }

    override fun getNoteById(id: String): Observable<Note> {
        if (noteSubjects[id] != null) {
            return combineNoteSignals(noteSubjects[id]!!, id)
        }

        val documentRef = createDocumentRef(id)
        val noteSubject = BehaviorSubject.create<Note>()
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

    private fun combineNoteSignals(remote: Observable<Note>, id: String): Observable<Note> {
        return updatingNoteSubject.switchMap { optNote ->
            if (optNote.isPresent && optNote.get().id == id) {
                remote.map { optNote.get() }
            } else {
                remote
            }
        }
    }

    override fun addNote(note: Note) {
        setNoteDocument(note)
    }

    override fun putNote(note: Note) {
        updatingNoteSubject.onNext(Optional.of(note))
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

    private fun setNoteDocument(note: Note) {
        val noteData = hashMapOf(
            FIELD_TEXT to note.text,
            FIELD_COLOR to note.color.color,
            FIELD_POSITION_X to note.position.x.toString(),
            FIELD_POSITION_Y to note.position.y.toString()
        )

        firestore.collection(COLLECTION_NOTES)
            .document(note.id)
            .set(noteData)
    }

    private fun documentToNotes(document: DocumentSnapshot?): Note? {
        val data: Map<String, Any> = document?.data as? Map<String, Any> ?: return null
        val text = data[FIELD_TEXT] as String
        val color = YBColor(data[FIELD_COLOR] as Long)
        val positionX = data[FIELD_POSITION_X] as String? ?: "0"
        val positionY = data[FIELD_POSITION_Y] as String? ?: "0"
        val position = Position(positionX.toFloat(), positionY.toFloat())
        return Note(document.id, text, position, color)
    }

    companion object {
        const val COLLECTION_NOTES = "Notes"
        const val FIELD_TEXT = "text"
        const val FIELD_COLOR = "color"
        const val FIELD_POSITION_X = "positionX"
        const val FIELD_POSITION_Y = "positionY"
    }
}