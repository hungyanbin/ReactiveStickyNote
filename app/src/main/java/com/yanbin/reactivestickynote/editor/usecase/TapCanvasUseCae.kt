package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.utils.mapOptional
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class TapCanvasUseCae(
    private val tapCanvasFlow: Flow<Unit>,
) {

    fun startFlow(editor: Editor): Flow<Any> {
        return tapCanvasFlow
            .map { editor.userSelectedNote.first() }
            .mapOptional { it }
            .onEach { selectedNote ->
                editor.setNoteUnSelected(selectedNote.noteId)
                editor.showAddButton()
            }
    }
}