package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.ContextMenuEvent
import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.reactivestickynote.stickynote.model.NoteAttribute
import com.yanbin.utils.filterInstance
import io.reactivex.rxjava3.kotlin.addTo

class ChangeColorUseCase : BaseEditorUseCase() {

    override fun start(editor: Editor, noteRepository: NoteRepository) {
        editor.contextMenu
            .contextMenuEvents
            .filterInstance<ContextMenuEvent.ChangeColor>()
            .withLatestFrom(editor.selectedStickyNote) { event, optSelectedNote ->
                optSelectedNote.map { note ->
                    note.id to event.color
                }
            }.mapOptional { it }
            .subscribe { (id, color) ->
                val attribute = NoteAttribute.Color(color)
                noteRepository.updateNote(id, listOf(attribute))
            }
            .addTo(disposableBag)
    }
}