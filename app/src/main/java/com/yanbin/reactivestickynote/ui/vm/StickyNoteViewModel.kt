package com.yanbin.reactivestickynote.ui.vm

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.domain.Editor
import com.yanbin.reactivestickynote.model.Position
import com.yanbin.reactivestickynote.model.StickyNote
import com.yanbin.utils.fold
import io.reactivex.rxjava3.core.Observable

class StickyNoteViewModel(
    private val editor: Editor
): ViewModel() {

    fun moveNote(noteId: String, positionDelta: Position) {
        editor.moveNote(noteId, positionDelta)
    }

    fun tapNote(stickyNote: StickyNote) {
        editor.selectNote(stickyNote.id)
    }

    fun getNoteById(id: String) = editor.getNoteById(id)

    fun isSelected(id: String): Observable<Boolean> {
        return editor.selectedNote
            .map { optNote ->
                optNote.fold(
                    someFun = { note -> note.id == id},
                    emptyFun = { false }
                )
            }
    }
}