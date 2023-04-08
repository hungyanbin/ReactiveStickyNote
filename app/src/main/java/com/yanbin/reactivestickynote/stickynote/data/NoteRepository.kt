package com.yanbin.reactivestickynote.stickynote.data

import com.yanbin.reactivestickynote.account.Account
import com.yanbin.reactivestickynote.stickynote.model.NoteAttribute
import com.yanbin.reactivestickynote.stickynote.model.SelectedNote
import com.yanbin.reactivestickynote.stickynote.model.StickyNote
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllVisibleNoteIds(): Flow<List<String>>
    fun getAllSelectedNotes(): Flow<List<SelectedNote>>
    fun getNoteById(id: String): Flow<StickyNote>
    suspend fun updateNote(noteId: String, attributes: List<NoteAttribute>)
    suspend fun createNote(stickyNote: StickyNote)
    suspend fun deleteNote(noteId: String)
    suspend fun setNoteSelection(noteId: String, account: Account)
    suspend fun removeNoteSelection(noteId: String, account: Account)
}