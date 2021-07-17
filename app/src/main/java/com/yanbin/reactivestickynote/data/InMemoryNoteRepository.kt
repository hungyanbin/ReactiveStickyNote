package com.yanbin.reactivestickynote.data

import com.yanbin.reactivestickynote.model.Note
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.ConcurrentHashMap

class InMemoryNoteRepository: NoteRepository {

    private val notes = BehaviorSubject.createDefault(emptyList<Note>())
    private val noteMap = ConcurrentHashMap<String, Note>()

    override fun getAllNotes(): Observable<List<Note>> = notes.hide()

    override fun putNote(note: Note) {
        noteMap[note.id] = note
        notes.onNext(noteMap.elements().toList())
    }

    override fun addNote(note: Note) {
        noteMap[note.id] = note
    }

    override fun deleteNote(noteId: String) {
        noteMap.remove(noteId)
    }

    init {
        Note.createRandomNote().let { note -> noteMap[note.id] = note }
        Note.createRandomNote().let { note -> noteMap[note.id] = note }
        notes.onNext(noteMap.elements().toList())
    }
}