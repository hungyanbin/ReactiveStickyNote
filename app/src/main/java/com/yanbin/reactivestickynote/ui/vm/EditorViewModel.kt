package com.yanbin.reactivestickynote.ui.vm

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.domain.Editor
import com.yanbin.reactivestickynote.model.StickyNote
import io.reactivex.rxjava3.core.Observable

class EditorViewModel(
    private val editor: Editor
): ViewModel() {

    val openEditTextScreen: Observable<StickyNote> = editor.openEditTextScreen
    val allVisibleNoteIds = editor.allVisibleNoteIds
    val showContextMenu = editor.showContextMenu
    val showAddButton = editor.showAddButton

    init {
        editor.start()
    }

    fun addNewNote() {
        editor.addNewNote()
    }

    fun tapCanvas() {
        editor.clearSelection()
    }

    override fun onCleared() {
        editor.stop()
    }

}