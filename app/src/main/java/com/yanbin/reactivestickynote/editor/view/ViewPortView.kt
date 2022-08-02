package com.yanbin.reactivestickynote.editor.view

import android.util.Log
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import com.yanbin.reactivestickynote.editor.model.Position

@Composable
fun ViewPortView(
    noteIds: List<String>,
    viewPortScale: Float,
    viewPortCenter: Position,
    onViewPortTransform: (Position, Float) -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .pointerInput("1") {
                detectTransformGestures { _, pan, zoom, _ ->
                    onViewPortTransform(Position(pan.x, pan.y), zoom)
                }
            }
            .offset { IntOffset(viewPortCenter.x.toInt(), viewPortCenter.y.toInt()) }
            .scale(viewPortScale)
    )
    {
        noteIds.forEach { id ->
            key(id) {
                StatefulStickyNoteView(
                    modifier = Modifier,
                    id = id
                )
            }
        }
    }
}
