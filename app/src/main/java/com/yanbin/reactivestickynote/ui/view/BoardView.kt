package com.yanbin.reactivestickynote.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun BoardView(
    noteIds: List<String>
) {
    Box(Modifier.fillMaxSize()) {
        noteIds.forEach { id ->
            key(id) {
                StatefulStickyNote(
                    modifier = Modifier.align(Alignment.Center),
                    id = id
                )
            }
        }
    }
}
