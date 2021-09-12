package com.yanbin.reactivestickynote.data

import com.yanbin.reactivestickynote.model.Note
import io.reactivex.rxjava3.core.Observable

interface NoteRepository {
    fun getAllNotes(): Observable<List<Note>>
    fun getNoteById(id: String): Observable<Note>
    fun putNote(note: Note)
    fun createNote(note: Note)
    fun deleteNote(noteId: String)
}
