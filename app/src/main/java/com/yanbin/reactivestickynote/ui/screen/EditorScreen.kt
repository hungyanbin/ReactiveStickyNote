package com.yanbin.reactivestickynote.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import com.yanbin.reactivestickynote.domain.EditorViewModel
import com.yanbin.reactivestickynote.ui.view.BoardView

@Composable
fun EditorScreen(
    viewModel: EditorViewModel,
) {
    Surface(color = MaterialTheme.colors.background) {
        Box(
            Modifier.fillMaxSize()
        ) {
            BoardView(
                viewModel.allNotes.subscribeAsState(initial = emptyList()),
                viewModel::moveNote
            )
        }
    }
}