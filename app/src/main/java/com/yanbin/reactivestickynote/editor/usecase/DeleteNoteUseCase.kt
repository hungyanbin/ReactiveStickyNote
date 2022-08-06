package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.ContextMenuEvent
import com.yanbin.reactivestickynote.editor.domain.StickyNoteEditor
import com.yanbin.utils.filterInstance
import io.reactivex.rxjava3.kotlin.addTo

class DeleteNoteUseCase : BaseEditorUseCase() {

    override fun start(stickyNoteEditor: StickyNoteEditor) {
        stickyNoteEditor.contextMenu
            .contextMenuEvents
            .filterInstance<ContextMenuEvent.DeleteNote>()
            .withLatestFrom(stickyNoteEditor.userSelectedNote) { _, optSelectedNote ->
                optSelectedNote.map { note ->
                    note.noteId
                }
            }.mapOptional { it }
            .subscribe { id ->
                stickyNoteEditor.setNoteUnSelected(id)
                stickyNoteEditor.removeNote(id)
                stickyNoteEditor.showAddButton()
            }
            .addTo(disposableBag)
    }
}