package com.yanbin.reactivestickynote.editor.view

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.yanbin.reactivestickynote.R
import com.yanbin.reactivestickynote.stickynote.model.Position
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
    val onSizeChanged: (Float, Float) -> Unit = { deltaX, deltaY ->
        stickyNoteViewModel.onChangeSize(id, deltaX, deltaY)
    }

    val noteUiModel by stickyNoteViewModel.getNoteById(id).subscribeAsState(initial = StickyNoteUiModel.emptyUiModel(id))

    StickyNoteView(
        modifier = modifier,
        onPositionChanged = onPositionChanged,
        onClick = stickyNoteViewModel::tapNote,
        onSizeChanged = onSizeChanged,
        stickyNoteUiModel = noteUiModel
    )
}

private val highlightBorder: @Composable Modifier.(Boolean, Color) -> Modifier = { show, color ->
    if (show) {
        this.border(2.dp, color, MaterialTheme.shapes.medium)
    } else {
        this
    }.padding(8.dp)
}


@Composable
fun StickyNoteView(
    modifier: Modifier = Modifier,
    onPositionChanged: (Position) -> Unit = {},
    onSizeChanged: (Float, Float) -> Unit = { _, _ -> },
    onClick: (String) -> Unit,
    stickyNoteUiModel: StickyNoteUiModel,
) {
    val noteSize = stickyNoteUiModel.stickyNote.size
    val noteWidth = noteSize.width
    val noteHeight = noteSize.height
    val density = LocalDensity.current

    val noteWidthDp by animateDpAsState(targetValue = with(density) { noteWidth.toDp() })
    val noteHeightDp by animateDpAsState(targetValue = with(density) { noteHeight.toDp() })

    val positionOffset by animateIntOffsetAsState(
        targetValue = IntOffset(
            stickyNoteUiModel.position.x.toInt(),
            stickyNoteUiModel.position.y.toInt()
        )
    )


    val selected = stickyNoteUiModel.isSelected
    val selectingUserName = stickyNoteUiModel.selectedUserName
    val selectedColor = if (stickyNoteUiModel.isLocked) Color.Red else Color.Black

    Surface(
        modifier.offset { positionOffset }
            .size(noteWidthDp, noteHeightDp)
            .highlightBorder(selected, selectedColor),
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
            color = selectedColor,
            modifier = modifier.offset { positionOffset.copy(y = positionOffset.y + noteHeight.toInt() + 8.dp.roundToPx()) }
        )

        if (!stickyNoteUiModel.isLocked) {
            Icon(
                modifier = modifier
                    .offset {
                        positionOffset.copy(
                            x = positionOffset.x + noteWidth.toInt() - 14.dp.roundToPx(),
                            y = positionOffset.y + noteHeight.toInt() - 14.dp.roundToPx()
                        )
                    }
                    .background(Color.White)
                    .border(0.dp, Color.Black)
                    .pointerInput(stickyNoteUiModel.id) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            onSizeChanged(dragAmount.x, dragAmount.y)
                        }
                    }
                    .rotate(90f)
                ,
                painter = painterResource(id = R.drawable.ic_scale),
                contentDescription = "")
        }
    }
}
