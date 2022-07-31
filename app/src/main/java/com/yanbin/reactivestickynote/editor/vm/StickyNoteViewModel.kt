package com.yanbin.reactivestickynote.editor.vm

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.editor.domain.StickyNoteEditor
import com.yanbin.reactivestickynote.editor.model.Position
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.Observables

class StickyNoteViewModel(
    private val stickyNoteEditor: StickyNoteEditor
): ViewModel() {

    fun moveNote(noteId: String, positionDelta: Position) {
        stickyNoteEditor.moveNote(noteId, positionDelta)
    }

    fun tapNote(id: String) {
        stickyNoteEditor.selectNote(id)
    }

    fun getNoteById(id: String): Observable<StickyNoteUiModel> = Observables.combineLatest(stickyNoteEditor.getNoteById(id), stickyNoteEditor.userSelectedNote)
        .map { (note, userSelectedNote) ->
            if (userSelectedNote.isPresent && note.id == userSelectedNote.get().noteId) {
                StickyNoteUiModel(note, StickyNoteUiModel.State.Selected(userSelectedNote.get().userName))
            } else {
                StickyNoteUiModel(note, StickyNoteUiModel.State.Normal)
            }
        }
}