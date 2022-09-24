package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.ContextMenuEvent
import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.utils.filterInstance
import io.reactivex.rxjava3.kotlin.addTo

class EditTextUseCase : BaseEditorUseCase() {

    override fun start(editor: Editor) {
        editor.contextMenu
            .contextMenuEvents
            .filterInstance<ContextMenuEvent.NavigateToEditTextPage>()
            .withLatestFrom(editor.selectedStickyNote) { _, selectedNote -> selectedNote}
            .mapOptional { it }
            .subscribe { stickyNote ->
                editor.navigateToEditTextPage(stickyNote)
            }
            .addTo(disposableBag)
    }
}