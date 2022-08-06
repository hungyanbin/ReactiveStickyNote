package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.ContextMenuEvent
import com.yanbin.reactivestickynote.editor.domain.StickyNoteEditor
import com.yanbin.utils.filterInstance
import io.reactivex.rxjava3.kotlin.addTo

class ChangeColorUseCase : BaseEditorUseCase() {

    override fun start(stickyNoteEditor: StickyNoteEditor) {
        stickyNoteEditor.contextMenu
            .contextMenuEvents
            .filterInstance<ContextMenuEvent.ChangeColor>()
            .withLatestFrom(stickyNoteEditor.selectedStickyNote) { event, optSelectedNote ->
                optSelectedNote.map { note ->
                    note.copy(color = event.color )
                }
            }.mapOptional { it }
            .subscribe { note ->
                stickyNoteEditor.updateNote(note)
            }
            .addTo(disposableBag)
    }
}