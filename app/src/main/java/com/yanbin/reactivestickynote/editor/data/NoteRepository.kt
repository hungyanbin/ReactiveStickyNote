package com.yanbin.reactivestickynote.editor.data

import com.yanbin.reactivestickynote.editor.model.StickyNote
import io.reactivex.rxjava3.core.Observable

interface NoteRepository {
    fun getAllVisibleNoteIds(): Observable<List<String>>
    fun getNoteById(id: String): Observable<StickyNote>
    fun putNote(stickyNote: StickyNote)
    fun createNote(stickyNote: StickyNote)
    fun deleteNote(noteId: String)
}
