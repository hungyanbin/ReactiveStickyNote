package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.reactivestickynote.stickynote.model.NoteAttribute
import com.yanbin.reactivestickynote.stickynote.model.Position
import com.yanbin.utils.mapOptional
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.rx3.asFlow

// Pair for noteId and position delta
typealias NotePositionDelta = Pair<String, Position>

class MoveNoteUseCase(
    private val noteMoveFlow: Flow<NotePositionDelta>,
) {

    fun startFlow(editor: Editor, noteRepository: NoteRepository): Flow<Any> {
        return noteMoveFlow
            .map { (noteId, delta) ->
                val note = editor.getNoteById(noteId).first()
                val optSelectedNote = editor.userSelectedNote.first()
                doOnUserSelectedNote(optSelectedNote, note) {
                    note.id to note.position + delta
                }
            }
            .mapOptional { it }
            .onEach { (noteId, newPosition) ->
                val attribute = NoteAttribute.Pos(newPosition)
                noteRepository.updateNote(noteId, listOf(attribute))
            }
    }
}