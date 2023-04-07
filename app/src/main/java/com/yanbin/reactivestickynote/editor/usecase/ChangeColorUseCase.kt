package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.ContextMenuEvent
import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.reactivestickynote.stickynote.model.NoteAttribute
import com.yanbin.utils.mapOptional
import kotlinx.coroutines.flow.*

class ChangeColorUseCase {
    fun startFlow(editor: Editor, noteRepository: NoteRepository): Flow<Any> {
        return editor.contextMenu
            .contextMenuEvents
            .filterIsInstance<ContextMenuEvent.ChangeColor>()
            .map { event ->
                val optSelectedNote = editor.selectedStickyNote.first()
                optSelectedNote.map { it.id to event.color }
            }
            .mapOptional { it }
            .onEach { (id, color) ->
                val attribute = NoteAttribute.Color(color)
                noteRepository.updateNote(id, listOf(attribute))
            }
    }
}