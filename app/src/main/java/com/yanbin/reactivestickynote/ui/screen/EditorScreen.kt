package com.yanbin.reactivestickynote.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yanbin.reactivestickynote.R
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

            FloatingActionButton(
                onClick = { viewModel.addNewNote() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            ) {
                val painter = painterResource(id = R.drawable.ic_add)
                Icon(painter = painter, contentDescription = "Add")
            }

        }

    }
}