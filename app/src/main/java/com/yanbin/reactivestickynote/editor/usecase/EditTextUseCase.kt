package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.ContextMenuEvent
import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.utils.mapOptional
import kotlinx.coroutines.flow.*

class EditTextUseCase {

    fun startFlow(editor: Editor): Flow<Any> {
        return editor.contextMenu
            .contextMenuEvents
            .filterIsInstance<ContextMenuEvent.NavigateToEditTextPage>()
            .map { editor.selectedStickyNote.first() }
            .mapOptional { it }
            .onEach { stickyNote ->
                editor.navigateToEditTextPage(stickyNote)
            }
    }
}