package com.yanbin.reactivestickynote.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yanbin.reactivestickynote.R
import com.yanbin.reactivestickynote.ui.vm.EditorViewModel
import com.yanbin.reactivestickynote.ui.view.BoardView
import com.yanbin.reactivestickynote.ui.view.StatefulMenuView
import com.yanbin.utils.subscribeBy
import com.yanbin.utils.toMain

@ExperimentalAnimationApi
@Composable
fun EditorScreen(
    viewModel: EditorViewModel,
    openEditTextScreen: (String) -> Unit
) {
    viewModel.openEditTextScreen
        .toMain()
        .subscribeBy (onNext = openEditTextScreen)

    Surface(color = MaterialTheme.colors.background) {
        Box(
            Modifier
                .fillMaxSize()
                .pointerInput("Editor") {
                    detectTapGestures { viewModel.tapCanvas() }
                }
        ) {
            val showContextMenu by viewModel.showContextMenu.subscribeAsState(initial = false)
            val showAddButton by viewModel.showAddButton.subscribeAsState(initial = true)
            val noteIdsState by viewModel.allVisibleNoteIds.subscribeAsState(initial = listOf())

            BoardView(noteIdsState)

            AnimatedVisibility(
                visible = showAddButton,
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                FloatingActionButton(
                    onClick = { viewModel.addNewNote() },
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    val painter = painterResource(id = R.drawable.ic_add)
                    Icon(painter = painter, contentDescription = "Add")
                }
            }

            AnimatedVisibility(
                visible = showContextMenu,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                StatefulMenuView()
            }

        }

    }
}