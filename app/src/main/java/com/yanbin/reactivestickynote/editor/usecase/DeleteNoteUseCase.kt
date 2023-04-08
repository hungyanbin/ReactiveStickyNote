package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.ContextMenuEvent
import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.utils.mapOptional
import kotlinx.coroutines.flow.*

class DeleteNoteUseCase {

    fun startFlow(editor: Editor, noteRepository: NoteRepository): Flow<Any> {
        return editor.contextMenu
            .contextMenuEvents
            .filterIsInstance<ContextMenuEvent.DeleteNote>()
            .map { editor.userSelectedNote.first().map { it.noteId } }
            .mapOptional { it }
            .onEach { id ->
                editor.setNoteUnSelected(id)
                noteRepository.deleteNote(id)
                editor.showAddButton()
            }
    }
}