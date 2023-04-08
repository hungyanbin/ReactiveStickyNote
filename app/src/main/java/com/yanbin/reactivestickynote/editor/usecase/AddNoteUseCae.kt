package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.reactivestickynote.stickynote.model.StickyNote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class AddNoteUseCae(
    private val tapAddFlow: Flow<Unit>
) {

    fun startFlow(noteRepository: NoteRepository): Flow<Any> {
        return tapAddFlow
            .onEach {
                val newNote = StickyNote.createRandomNote()
                noteRepository.createNote(newNote)
            }
    }
}