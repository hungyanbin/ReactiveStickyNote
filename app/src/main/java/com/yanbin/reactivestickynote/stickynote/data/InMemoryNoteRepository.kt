package com.yanbin.reactivestickynote.stickynote.data

import com.yanbin.reactivestickynote.account.Account
import com.yanbin.reactivestickynote.stickynote.model.NoteAttribute
import com.yanbin.reactivestickynote.stickynote.model.SelectedNote
import com.yanbin.reactivestickynote.stickynote.model.StickyNote
import com.yanbin.utils.mapOptional
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class InMemoryNoteRepository: NoteRepository {

    private val notesFlow = MutableStateFlow(emptyList<StickyNote>())
    private val noteMap = ConcurrentHashMap<String, StickyNote>()
    private val selectedNotesFlow = MutableStateFlow(emptyList<SelectedNote>())
    private val selectedNoteMap = ConcurrentHashMap<String, SelectedNote>()

    override suspend fun updateNote(noteId: String, attributes: List<NoteAttribute>) = withContext(Dispatchers.IO) {
        noteMap[noteId] = UpdatedNoteAttributes.fromAttributes(noteId, attributes).updateNote(noteMap[noteId]!!)
        sendNotesUpdateSignal()
    }

    override suspend fun createNote(note: StickyNote) = withContext(Dispatchers.IO) {
        noteMap[note.id] = note
        sendNotesUpdateSignal()
    }

    override suspend fun deleteNote(noteId: String) = withContext(Dispatchers.IO) {
        noteMap.remove(noteId)
        sendNotesUpdateSignal()
    }

    override fun getNoteById(id: String): Flow<StickyNote> {
        return notesFlow.map { notes ->
            Optional.ofNullable(notes.find { note -> note.id == id })
        }.mapOptional { it }
    }

    override fun getAllVisibleNoteIds(): Flow<List<String>> {
        return notesFlow.map { notes -> notes.map { it.id } }
    }

    override fun getAllSelectedNotes(): Flow<List<SelectedNote>> {
        return selectedNotesFlow
    }

    override suspend fun setNoteSelection(noteId: String, account: Account) = withContext(Dispatchers.IO) {
        selectedNoteMap[account.id] = SelectedNote(noteId, account.userName)
        sendSelectedNotesUpdateSignal()
    }

    override suspend fun removeNoteSelection(noteId: String, account: Account) = withContext(Dispatchers.IO) {
        selectedNoteMap.remove(account.id)
        sendSelectedNotesUpdateSignal()
    }

    init {
        StickyNote.createRandomNote().let { note -> noteMap[note.id] = note }
        StickyNote.createRandomNote().let { note -> noteMap[note.id] = note }
        sendNotesUpdateSignal()
    }

    private fun sendNotesUpdateSignal() {
        notesFlow.tryEmit(noteMap.elements().toList())
    }

    private fun sendSelectedNotesUpdateSignal() {
        selectedNotesFlow.tryEmit(selectedNoteMap.elements().toList())
    }
}