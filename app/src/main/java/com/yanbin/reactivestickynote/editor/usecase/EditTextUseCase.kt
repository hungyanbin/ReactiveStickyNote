package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.ContextMenuEvent
import com.yanbin.reactivestickynote.editor.domain.StickyNoteEditor
import com.yanbin.utils.filterInstance
import io.reactivex.rxjava3.kotlin.addTo

class EditTextUseCase : BaseEditorUseCase() {

    override fun start(stickyNoteEditor: StickyNoteEditor) {
        stickyNoteEditor.contextMenu
            .contextMenuEvents
            .filterInstance<ContextMenuEvent.NavigateToEditTextPage>()
            .withLatestFrom(stickyNoteEditor.selectedNote) { _, selectedNote -> selectedNote}
            .mapOptional { it }
            .subscribe { stickyNote ->
                stickyNoteEditor.navigateToEditTextPage(stickyNote)
            }
            .addTo(disposableBag)
    }
}