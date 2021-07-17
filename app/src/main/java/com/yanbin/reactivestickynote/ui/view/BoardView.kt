package com.yanbin.reactivestickynote.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReusableContent
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yanbin.reactivestickynote.model.Note
import com.yanbin.reactivestickynote.model.Position
import java.util.*

@Composable
fun BoardView(
    notesState: State<List<Note>>,
    selectedNoteState: State<Optional<Note>>,
    updateNotePosition: (String, Position) -> Unit,
    onNoteClicked: (Note) -> Unit
) {
    val notes by notesState
    val selectedNote by selectedNoteState

    Box(Modifier.fillMaxSize()) {
        notes.forEach { note ->
            val onNotePositionChanged: (Position) -> Unit = { delta ->
                updateNotePosition(note.id, delta)
            }

            val selected = selectedNote.filter { it.id == note.id }.isPresent

            ReusableContent(key = note.id) {
                StickyNote(
                    modifier = Modifier.align(Alignment.Center),
                    note = note,
                    selected = selected,
                    onPositionChanged = onNotePositionChanged,
                    onClick = onNoteClicked
                )
            }
        }
    }
}
