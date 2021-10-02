package com.yanbin.reactivestickynote.ui.vm

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.domain.CoEditor
import com.yanbin.reactivestickynote.model.Position
import com.yanbin.reactivestickynote.model.StickyNote
import com.yanbin.utils.fold
import io.reactivex.rxjava3.core.Observable

class StickyNoteViewModel(
    private val coEditor: CoEditor
): ViewModel() {

    fun moveNote(noteId: String, positionDelta: Position) {
        coEditor.moveNote(noteId, positionDelta)
    }

    fun tapNote(stickyNote: StickyNote) {
        coEditor.selectNote(stickyNote.id)
    }

    fun getNoteById(id: String) = coEditor.getNoteById(id)

    fun isSelected(id: String): Observable<Boolean> {
        return coEditor.selectedNote
            .map { optNote ->
                optNote.fold(
                    someFun = { note -> note.id == id},
                    emptyFun = { false }
                )
            }
    }
}