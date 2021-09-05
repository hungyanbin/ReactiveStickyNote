package com.yanbin.reactivestickynote.data

import com.yanbin.reactivestickynote.model.Note
import io.reactivex.rxjava3.core.Observable

interface NoteRepository {
    fun getAllVisibleNoteIds(): Observable<List<String>>
    fun getNoteById(id: String): Observable<Note>
    fun putNote(note: Note)
    fun createNote(note: Note)
    fun deleteNote(noteId: String)
}
