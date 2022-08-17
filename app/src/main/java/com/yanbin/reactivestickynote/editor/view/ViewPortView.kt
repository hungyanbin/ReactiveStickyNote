package com.yanbin.reactivestickynote.editor.view

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.yanbin.reactivestickynote.editor.model.Position
import com.yanbin.reactivestickynote.editor.model.YBColor
import com.yanbin.reactivestickynote.editor.vm.ContextMenuViewModel
import com.yanbin.reactivestickynote.editor.vm.ViewPortViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@Composable
fun StatefulViewPortView() {
    val viewPortViewModel by LocalViewModelStoreOwner.current!!.viewModel<ViewPortViewModel>()
    val scale by viewPortViewModel.scale.subscribeAsState(initial = 0f)
    val center by viewPortViewModel.center.subscribeAsState(initial = Position(0f, 0f))
    val noteIds by viewPortViewModel.allVisibleNoteIds.subscribeAsState(initial = emptyList())

    ViewPortView(
        noteIds = noteIds,
        viewPortScale = scale,
        viewPortCenter = center,
        onViewPortTransform = viewPortViewModel::transformViewPort
    )
}

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
