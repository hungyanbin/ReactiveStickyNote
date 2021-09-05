package com.yanbin.reactivestickynote.data

import com.yanbin.reactivestickynote.model.Note
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class InMemoryNoteRepository: NoteRepository {

    private val notes = BehaviorSubject.createDefault(emptyList<Note>())
    private val noteMap = ConcurrentHashMap<String, Note>()

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

    override fun getNoteById(id: String): Observable<Note> {
        return notes.map { notes ->
            Optional.ofNullable(notes.find { note -> note.id == id })
        }.mapOptional { it }
    }

    override fun getAllVisibleNoteIds(): Observable<List<String>> {
        return notes.map { notes -> notes.map { it.id } }
    }

    init {
        Note.createRandomNote().let { note -> noteMap[note.id] = note }
        Note.createRandomNote().let { note -> noteMap[note.id] = note }
        notes.onNext(noteMap.elements().toList())
    }
}