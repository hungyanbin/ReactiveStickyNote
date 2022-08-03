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

    fun onChangeSize(noteId: String, xDelta: Float, yDelta: Float) {
        stickyNoteEditor.changeNoteSize(noteId, xDelta, yDelta)
    }

    fun tapNote(id: String) {
        stickyNoteEditor.selectNote(id)
    }

    fun getNoteById(id: String): Observable<StickyNoteUiModel> = Observables.combineLatest(stickyNoteEditor.getNoteById(id), stickyNoteEditor.selectedNotes)
        .map { (note, selectedNotes) ->
            val selectedNote = selectedNotes.find { it.noteId == note.id }
            if (selectedNote != null) {
                StickyNoteUiModel(note, StickyNoteUiModel.State.Selected(selectedNote.userName))
            } else {
                StickyNoteUiModel(note, StickyNoteUiModel.State.Normal)
            }
        }
}