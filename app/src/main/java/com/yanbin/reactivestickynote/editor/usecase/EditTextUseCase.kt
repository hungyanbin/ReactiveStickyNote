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
            .subscribe {
                stickyNoteEditor.navigateToEditTextPage()
            }
            .addTo(disposableBag)
    }
}