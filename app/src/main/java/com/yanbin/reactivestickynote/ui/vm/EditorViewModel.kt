package com.yanbin.reactivestickynote.ui.vm

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.domain.CoEditor
import io.reactivex.rxjava3.core.Observable

class EditorViewModel(
    private val coEditor: CoEditor
): ViewModel() {

    val allVisibleNoteIds: Observable<List<String>> = coEditor.allVisibleNotes
    val openEditTextScreen: Observable<String> = coEditor.openEditTextScreen
    val showContextMenu = coEditor.showContextMenu
    val showAddButton = coEditor.showAdderButton

    init {
        coEditor.start()
    }

    fun addNewNote() {
        coEditor.addNewNote()
    }

    fun tapCanvas() {
        coEditor.clearSelection()
    }

    override fun onCleared() {
        coEditor.stop()
    }

}