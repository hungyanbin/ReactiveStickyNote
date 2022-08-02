package com.yanbin.reactivestickynote.editor.view

import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.yanbin.reactivestickynote.editor.model.Position
import com.yanbin.reactivestickynote.editor.vm.StickyNoteUiModel
import com.yanbin.reactivestickynote.editor.vm.StickyNoteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@Composable
fun StatefulStickyNoteView(
    id: String,
    modifier: Modifier = Modifier,
) {
    val stickyNoteViewModel by LocalViewModelStoreOwner.current!!.viewModel<StickyNoteViewModel>()
    val onPositionChanged: (Position) -> Unit = { delta ->
        stickyNoteViewModel.moveNote(id, delta)
    }
    val noteUiModel by stickyNoteViewModel.getNoteById(id).subscribeAsState(initial = StickyNoteUiModel.emptyUiModel(id))

    StickyNoteView(
        modifier = modifier,
        onPositionChanged = onPositionChanged,
        onClick = stickyNoteViewModel::tapNote,
        stickyNoteUiModel = noteUiModel
    )
}

private val highlightBorder: @Composable Modifier.(Boolean) -> Modifier = { show ->
    if (show) {
        this.border(2.dp, Color.Black, MaterialTheme.shapes.medium)
    } else {
        this
    }.padding(8.dp)
}


@Composable
fun StickyNoteView(
    modifier: Modifier = Modifier,
    onPositionChanged: (Position) -> Unit = {},
    onClick: (String) -> Unit,
    stickyNoteUiModel: StickyNoteUiModel,
) {
    val offset by animateIntOffsetAsState(
        targetValue = IntOffset(
            stickyNoteUiModel.position.x.toInt(),
            stickyNoteUiModel.position.y.toInt()
        )
    )

    val selected = stickyNoteUiModel.isSelected
    val selectingUserName = stickyNoteUiModel.selectedUserName

    Surface(
        modifier.offset { offset }
            .size(108.dp, 108.dp)
            .highlightBorder(selected),
        color = Color(stickyNoteUiModel.color.color),
        elevation = 8.dp
    ) {
        Column(modifier = Modifier
            .clickable { onClick(stickyNoteUiModel.id) }
            .pointerInput(stickyNoteUiModel.id) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onPositionChanged(Position(dragAmount.x, dragAmount.y))
                }
            }
            .padding(16.dp)
        ) {
            Text(text = stickyNoteUiModel.text, style = MaterialTheme.typography.h5)
        }
    }

    if (selectingUserName.isNotEmpty()) {
        Text(
            text = selectingUserName,
            modifier = modifier.offset { offset.copy(y = offset.y + 108.dp.roundToPx()) }
        )
    }
}
