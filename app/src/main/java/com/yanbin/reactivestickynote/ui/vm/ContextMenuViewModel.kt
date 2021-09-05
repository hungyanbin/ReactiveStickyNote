package com.yanbin.reactivestickynote.ui.vm

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.domain.ContextMenu
import com.yanbin.reactivestickynote.model.YBColor

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