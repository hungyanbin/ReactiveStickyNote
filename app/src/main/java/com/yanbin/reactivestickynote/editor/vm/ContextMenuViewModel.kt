package com.yanbin.reactivestickynote.editor.vm

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.editor.domain.ContextMenu
import com.yanbin.reactivestickynote.stickynote.model.YBColor

class ContextMenuViewModel(
    private val contextMenu: ContextMenu
): ViewModel() {

    val selectedColor = contextMenu.selectedColor
    val colorOptions = contextMenu.colorOptions

    fun onDeleteClicked() {
        contextMenu.onDeleteClicked()
    }

    fun onColorSelected(color: YBColor) {
        contextMenu.onColorSelected(color)
    }

    fun onEditTextClicked() {
        contextMenu.onEditTextClicked()
    }
}