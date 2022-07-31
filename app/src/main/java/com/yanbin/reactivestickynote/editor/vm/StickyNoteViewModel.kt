package com.yanbin.reactivestickynote.editor.vm

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.editor.domain.StickyNoteEditor
import com.yanbin.reactivestickynote.editor.model.Position
import com.yanbin.reactivestickynote.editor.model.StickyNote
import com.yanbin.utils.fold
import io.reactivex.rxjava3.core.Observable

class StickyNoteViewModel(
    private val stickyNoteEditor: StickyNoteEditor
): ViewModel() {

    fun moveNote(noteId: String, positionDelta: Position) {
        stickyNoteEditor.moveNote(noteId, positionDelta)
    }

    fun tapNote(id: String) {
        stickyNoteEditor.selectNote(id)
    }

    fun getNoteById(id: String): Observable<StickyNoteUiModel> = stickyNoteEditor.getNoteById(id)
        .map { StickyNoteUiModel(it, StickyNoteUiModel.State.Normal) }

    fun isSelected(id: String): Observable<Boolean> {
        return stickyNoteEditor.selectedNote
            .map { optNote ->
                optNote.fold(
                    someFun = { note -> note.id == id},
                    emptyFun = { false }
                )
            }
    }
}