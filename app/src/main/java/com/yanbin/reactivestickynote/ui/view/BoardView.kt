package com.yanbin.reactivestickynote.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yanbin.reactivestickynote.model.Note
import com.yanbin.reactivestickynote.model.Position

@Composable
fun BoardView(
    notesState: State<List<Note>>,
    updateNotePosition: (String, Position) -> Unit,
) {
    val notes by notesState
    Box(Modifier.fillMaxSize()) {
        notes.forEach { note ->
            val onNotePositionChanged: (Position) -> Unit = { delta ->
                updateNotePosition(note.id, delta)
            }

            StickyNote(
                modifier = Modifier.align(Alignment.Center),
                note = note,
                onPositionChanged = onNotePositionChanged,
            )
        }
    }
}
