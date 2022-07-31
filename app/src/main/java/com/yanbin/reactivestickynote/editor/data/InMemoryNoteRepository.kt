package com.yanbin.reactivestickynote.editor.data

import com.yanbin.reactivestickynote.editor.model.SelectedNote
import com.yanbin.reactivestickynote.editor.model.StickyNote
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class InMemoryNoteRepository: NoteRepository {

    private val notes = BehaviorSubject.createDefault(emptyList<StickyNote>())
    private val noteMap = ConcurrentHashMap<String, StickyNote>()
    private val selectedNotes = BehaviorSubject.createDefault(emptyList<SelectedNote>())
    private val selectedNoteMap = ConcurrentHashMap<String, SelectedNote>()

    override fun putNote(stickyNote: StickyNote) {
        noteMap[stickyNote.id] = stickyNote
        notes.onNext(noteMap.elements().toList())
    }

    override fun createNote(note: StickyNote) {
        noteMap[note.id] = note
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

    override fun getAllSelectedNotes(): Observable<List<SelectedNote>> {
        return selectedNotes.hide()
    }

    override fun addNoteSelection(noteId: String, userName: String) {
        selectedNoteMap[noteId] = SelectedNote(noteId, userName)
        selectedNotes.onNext(selectedNoteMap.elements().toList())
    }

    override fun removeNoteSelection(noteId: String) {
        selectedNoteMap.remove(noteId)
        selectedNotes.onNext(selectedNoteMap.elements().toList())
    }

    init {
        StickyNote.createRandomNote().let { note -> noteMap[note.id] = note }
        StickyNote.createRandomNote().let { note -> noteMap[note.id] = note }
        notes.onNext(noteMap.elements().toList())
    }
}