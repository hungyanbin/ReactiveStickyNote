package com.yanbin.reactivestickynote.ui.vm

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.domain.StickyNoteEditor
import com.yanbin.reactivestickynote.model.StickyNote
import io.reactivex.rxjava3.core.Observable

class EditorViewModel(
    private val stickyNoteEditor: StickyNoteEditor
): ViewModel() {

    val openEditTextScreen: Observable<StickyNote> = stickyNoteEditor.openEditTextScreen
    val allVisibleNoteIds = stickyNoteEditor.allVisibleNotes
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