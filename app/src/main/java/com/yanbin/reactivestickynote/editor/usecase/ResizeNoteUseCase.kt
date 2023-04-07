package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.common.YBSize
import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.reactivestickynote.stickynote.model.NoteAttribute
import com.yanbin.reactivestickynote.stickynote.model.StickyNote
import com.yanbin.utils.mapOptional
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.rx3.asFlow

// Triple for noteId, widthDelta and heightDelta
typealias NoteSizeDelta = Triple<String, Float, Float>

class ResizeNoteUseCase(
    private val noteResizeFlow: Flow<NoteSizeDelta>,
) {

    fun startFlow(editor: Editor, noteRepository: NoteRepository): Flow<Any> {
        return noteResizeFlow
            .map { (noteId, widthDelta, heightDelta) ->
                val note = editor.getNoteById(noteId).first()
                val optSelectedNote = editor.userSelectedNote.first()

                doOnUserSelectedNote(optSelectedNote, note) {
                    note.id to changeNoteSizeWithConstraint(note, widthDelta, heightDelta)
                }
            }
            .mapOptional { it }
            .onEach { (id, size) ->
                val attribute = NoteAttribute.Size(size)
                noteRepository.updateNote(id, listOf(attribute))
            }
    }

    private fun changeNoteSizeWithConstraint(note: StickyNote, widthDelta: Float, heightDelta: Float): YBSize {
        val currentSize = note.size
        val newWidth = (currentSize.width + widthDelta).coerceAtLeast(StickyNote.MIN_SIZE)
        val newHeight = (currentSize.height + heightDelta).coerceAtLeast(StickyNote.MIN_SIZE)
        return YBSize(newWidth, newHeight)
    }
}