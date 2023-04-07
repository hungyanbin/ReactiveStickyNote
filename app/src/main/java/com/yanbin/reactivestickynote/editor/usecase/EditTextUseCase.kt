package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.ContextMenuEvent
import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.reactivestickynote.stickynote.data.OldNoteRepository
import com.yanbin.utils.filterInstance
import com.yanbin.utils.mapOptional
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx3.asObservable

class EditTextUseCase(
    private val scope: CoroutineScope,
) : BaseEditorUseCase() {

    override fun start(editor: Editor, noteRepository: NoteRepository) {
        editor.contextMenu
            .contextMenuEvents
            .filterIsInstance<ContextMenuEvent.NavigateToEditTextPage>()
            .map { editor.selectedStickyNote.first() }
            .mapOptional { it }
            .onEach { stickyNote ->
                editor.navigateToEditTextPage(stickyNote)
            }
            .launchIn(scope)
    }
}