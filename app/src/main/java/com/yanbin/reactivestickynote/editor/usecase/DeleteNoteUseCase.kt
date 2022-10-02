package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.ContextMenuEvent
import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.utils.filterInstance
import io.reactivex.rxjava3.kotlin.addTo

class DeleteNoteUseCase : BaseEditorUseCase() {

    override fun start(editor: Editor) {
        editor.contextMenu
            .contextMenuEvents
            .filterInstance<ContextMenuEvent.DeleteNote>()
            .withLatestFrom(editor.userSelectedNote) { _, optSelectedNote ->
                optSelectedNote.map { note ->
                    note.noteId
                }
            }.mapOptional { it }
            .subscribe { id ->
                editor.setNoteUnSelected(id)
                editor.removeNote(id)
                editor.showAddButton()
            }
            .addTo(disposableBag)
    }
}