package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.reactivestickynote.stickynote.data.OldNoteRepository
import com.yanbin.reactivestickynote.stickynote.model.StickyNote
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AddNoteUseCae(
    private val tapAddObservable: Observable<Unit>,
    private val coroutineScope: CoroutineScope
): BaseEditorUseCase() {

    override fun start(editor: Editor, noteRepository: NoteRepository) {
        tapAddObservable.subscribe {
            val newNote = StickyNote.createRandomNote()
            coroutineScope.launch {
                noteRepository.createNote(newNote)
            }
        }.addTo(disposableBag)
    }
}