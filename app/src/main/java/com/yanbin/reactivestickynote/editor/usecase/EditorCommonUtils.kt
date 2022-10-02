package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.stickynote.model.SelectedNote
import com.yanbin.reactivestickynote.stickynote.model.StickyNote
import com.yanbin.utils.fold
import java.util.*

fun <R> doOnUserSelectedNote(
    userSelectedNote: Optional<SelectedNote>,
    targetNote: StickyNote,
    action: () -> R
): Optional<R> {
    return userSelectedNote.fold(
        someFun = { selectedNote ->
            // Can move
            if (selectedNote.noteId == targetNote.id) {
                action()
            } else {
                null
            }
        },
        emptyFun = {
            null
        }
    ).let { Optional.ofNullable(it) }
}