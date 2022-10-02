package com.yanbin.reactivestickynote.stickynote.data

import com.yanbin.reactivestickynote.account.Account
import com.yanbin.reactivestickynote.stickynote.model.SelectedNote
import com.yanbin.reactivestickynote.stickynote.model.StickyNote
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
        sendNotesUpdateSignal()
    }

    override fun createNote(note: StickyNote) {
        noteMap[note.id] = note
        sendNotesUpdateSignal()
    }

    override fun deleteNote(noteId: String) {
        noteMap.remove(noteId)
        sendNotesUpdateSignal()
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

    override fun setNoteSelection(noteId: String, account: Account) {
        selectedNoteMap[account.id] = SelectedNote(noteId, account.userName)
        sendSelectedNotesUpdateSignal()
    }

    override fun removeNoteSelection(noteId: String, account: Account) {
        selectedNoteMap.remove(account.id)
        sendSelectedNotesUpdateSignal()
    }

    init {
        StickyNote.createRandomNote().let { note -> noteMap[note.id] = note }
        StickyNote.createRandomNote().let { note -> noteMap[note.id] = note }
        sendNotesUpdateSignal()
    }

    private fun sendNotesUpdateSignal() {
        notes.onNext(noteMap.elements().toList())
    }

    private fun sendSelectedNotesUpdateSignal() {
        selectedNotes.onNext(selectedNoteMap.elements().toList())
    }
}