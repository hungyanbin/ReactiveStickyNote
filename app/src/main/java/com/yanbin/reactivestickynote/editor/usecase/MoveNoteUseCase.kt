package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.StickyNoteEditor
import com.yanbin.reactivestickynote.editor.model.Position
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo

// Pair for noteId and position delta
typealias NotePositionDelta = Pair<String, Position>

class MoveNoteUseCase(
    private val noteMoveObservable: Observable<NotePositionDelta>
): BaseEditorUseCase() {

    override fun start(stickyNoteEditor: StickyNoteEditor) {
        val noteObservable = noteMoveObservable
            .switchMap { (noteId, _) -> stickyNoteEditor.getNoteById(noteId) }
        val deltaObservable = noteMoveObservable.map { (_, delta) -> delta }

        deltaObservable.withLatestFrom(stickyNoteEditor.userSelectedNote, noteObservable) { delta, optSelectedNote, note ->
            doOnUserSelectedNote(optSelectedNote, note) {
                note.copy(position = note.position + delta)
            }
        }
            .mapOptional { it }
            .subscribe { note ->
                stickyNoteEditor.updateNote(note)
            }
            .addTo(disposableBag)
    }
}