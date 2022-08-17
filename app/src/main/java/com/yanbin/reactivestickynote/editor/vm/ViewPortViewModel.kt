package com.yanbin.reactivestickynote.editor.vm

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.editor.domain.ViewPort
import com.yanbin.reactivestickynote.editor.model.Position

class ViewPortViewModel(
    private val viewPort: ViewPort
): ViewModel() {

    val allVisibleNoteIds = viewPort.allVisibleNoteIds

    val scale = viewPort.scale
    val center = viewPort.center

    fun transformViewPort(position: Position, scale: Float) {
        viewPort.transformDelta(position, scale)
    }
}