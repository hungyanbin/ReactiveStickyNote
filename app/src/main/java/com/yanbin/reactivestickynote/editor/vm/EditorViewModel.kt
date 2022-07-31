package com.yanbin.reactivestickynote.editor.vm

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.editor.domain.StickyNoteEditor
import com.yanbin.reactivestickynote.editor.model.StickyNote
import io.reactivex.rxjava3.core.Observable

class EditorViewModel(
    private val stickyNoteEditor: StickyNoteEditor
): ViewModel() {

    val openEditTextScreen: Observable<StickyNote> = stickyNoteEditor.openEditTextScreen
    val allVisibleNoteIds = stickyNoteEditor.allVisibleNoteIds
    val showContextMenu = stickyNoteEditor.showContextMenu
    val showAddButton = stickyNoteEditor.showAddButton

    init {
        stickyNoteEditor.start()
    }

    fun addNewNote() {
        stickyNoteEditor.addNewNote()
    }

    fun tapCanvas() {
        stickyNoteEditor.clearSelection()
    }

    override fun onCleared() {
        stickyNoteEditor.stop()
    }

}