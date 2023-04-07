package com.yanbin.reactivestickynote.editor.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanbin.reactivestickynote.editor.domain.ContextMenu
import com.yanbin.reactivestickynote.stickynote.model.YBColor
import kotlinx.coroutines.launch

class ContextMenuViewModel(
    private val contextMenu: ContextMenu
): ViewModel() {

    val selectedColor = contextMenu.selectedColor
    val colorOptions = contextMenu.colorOptions

    fun onDeleteClicked() = viewModelScope.launch {
        contextMenu.onDeleteClicked()
    }

    fun onColorSelected(color: YBColor) = viewModelScope.launch {
        contextMenu.onColorSelected(color)
    }

    fun onEditTextClicked() = viewModelScope.launch {
        contextMenu.onEditTextClicked()
    }
}