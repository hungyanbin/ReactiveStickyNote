package com.yanbin.reactivestickynote.editor.vm

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.editor.domain.StickyNoteEditor
import com.yanbin.reactivestickynote.editor.model.Position
import com.yanbin.reactivestickynote.editor.model.StickyNote
import io.reactivex.rxjava3.core.Observable

class EditorViewModel(
    private val stickyNoteEditor: StickyNoteEditor
): ViewModel() {

    val openEditTextScreen: Observable<StickyNote> = stickyNoteEditor.openEditTextScreen
    val allVisibleNoteIds = stickyNoteEditor.allVisibleNoteIds
    val showContextMenu = stickyNoteEditor.showContextMenu
    val showAddButton = stickyNoteEditor.showAddButton

    val viewPortScale = stickyNoteEditor.viewPort.scale
    val viewPortCenter = stickyNoteEditor.viewPort.center

    init {
        stickyNoteEditor.start()
    }

    fun addNewNote() {
        stickyNoteEditor.addNewNote()
    }

    fun tapCanvas() {
        stickyNoteEditor.clearSelection()
    }

    fun transformViewPort(position: Position, scale: Float) {
        stickyNoteEditor.viewPort.transformDelta(position, scale)
    }

    override fun onCleared() {
        stickyNoteEditor.stop()
    }

}