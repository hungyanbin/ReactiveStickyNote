package com.yanbin.reactivestickynote.data

import com.yanbin.reactivestickynote.model.StickyNote
import io.reactivex.rxjava3.core.Observable

interface NoteRepository {
    fun getAllVisibleNoteIds(): Observable<List<String>>
    fun getNoteById(id: String): Observable<StickyNote>
    fun putNote(stickyNote: StickyNote)
    fun addNote(stickyNote: StickyNote)
    fun deleteNote(noteId: String)
}
