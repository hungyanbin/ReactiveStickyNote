package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.reactivestickynote.stickynote.model.StickyNote
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.rx3.asFlow

class AddNoteUseCae(
    private val tapAddObservable: Observable<Unit>
) {

    fun startFlow(noteRepository: NoteRepository): Flow<Any> {
        return tapAddObservable.asFlow()
            .onEach {
                val newNote = StickyNote.createRandomNote()
                noteRepository.createNote(newNote)
            }
    }
}