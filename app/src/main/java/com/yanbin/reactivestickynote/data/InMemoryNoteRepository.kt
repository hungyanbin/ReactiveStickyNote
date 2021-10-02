package com.yanbin.reactivestickynote.data

import com.yanbin.reactivestickynote.model.StickyNote
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class InMemoryNoteRepository: NoteRepository {

    private val notes = BehaviorSubject.createDefault(emptyList<StickyNote>())
    private val noteMap = ConcurrentHashMap<String, StickyNote>()

    override fun putNote(stickyNote: StickyNote) {
        noteMap[stickyNote.id] = stickyNote
        notes.onNext(noteMap.elements().toList())
    }

    override fun addNote(stickyNote: StickyNote) {
        noteMap[stickyNote.id] = stickyNote
    }

    override fun deleteNote(noteId: String) {
        noteMap.remove(noteId)
    }

    override fun getNoteById(id: String): Observable<StickyNote> {
        return notes.map { notes ->
            Optional.ofNullable(notes.find { note -> note.id == id })
        }.mapOptional { it }
    }

    override fun getAllVisibleNoteIds(): Observable<List<String>> {
        return notes.map { notes -> notes.map { it.id } }
    }

    init {
        StickyNote.createRandomNote().let { note -> noteMap[note.id] = note }
        StickyNote.createRandomNote().let { note -> noteMap[note.id] = note }
        notes.onNext(noteMap.elements().toList())
    }
}