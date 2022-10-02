package com.yanbin.reactivestickynote.editor.vm

import com.yanbin.reactivestickynote.stickynote.model.StickyNote

data class StickyNoteUiModel(
    val stickyNote: StickyNote,
    val state: State
) {

    val id = stickyNote.id
    val position = stickyNote.position
    val color = stickyNote.color
    val text = stickyNote.text
    val isSelected = state is State.Selected
    val isLocked = state is State.Selected && state.isLocked
    val selectedUserName = if (state is State.Selected) {
        state.displayName
    } else {
        ""
    }

    sealed class State {
        class Selected(val displayName: String, val isLocked: Boolean): State()
        object Normal: State()
    }

    companion object {

        fun emptyUiModel(id: String): StickyNoteUiModel {
            val stickyNote = StickyNote.createEmptyNote(id)
            return StickyNoteUiModel(stickyNote, State.Normal)
        }
    }
}

