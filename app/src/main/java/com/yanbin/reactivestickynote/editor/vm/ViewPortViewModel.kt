package com.yanbin.reactivestickynote.editor.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanbin.reactivestickynote.editor.domain.ViewPort
import com.yanbin.reactivestickynote.stickynote.model.Position
import kotlinx.coroutines.launch

class ViewPortViewModel(
    private val viewPort: ViewPort
): ViewModel() {

    val allVisibleNoteIds = viewPort.allVisibleNoteIds

    val scale = viewPort.scale
    val center = viewPort.center

    fun transformViewPort(position: Position, scale: Float) = viewModelScope.launch {
        viewPort.transformDelta(position, scale)
    }
}