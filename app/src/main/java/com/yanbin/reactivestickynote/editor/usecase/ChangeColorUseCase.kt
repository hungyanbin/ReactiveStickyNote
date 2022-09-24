package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.ContextMenuEvent
import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.utils.filterInstance
import io.reactivex.rxjava3.kotlin.addTo

class ChangeColorUseCase : BaseEditorUseCase() {

    override fun start(editor: Editor) {
        editor.contextMenu
            .contextMenuEvents
            .filterInstance<ContextMenuEvent.ChangeColor>()
            .withLatestFrom(editor.selectedStickyNote) { event, optSelectedNote ->
                optSelectedNote.map { note ->
                    note.copy(color = event.color )
                }
            }.mapOptional { it }
            .subscribe { note ->
                editor.updateNote(note)
            }
            .addTo(disposableBag)
    }
}