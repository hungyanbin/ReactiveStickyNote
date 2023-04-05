package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.reactivestickynote.stickynote.model.NoteAttribute
import com.yanbin.reactivestickynote.stickynote.model.Position
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo

// Pair for noteId and position delta
typealias NotePositionDelta = Pair<String, Position>

class MoveNoteUseCase(
    private val noteMoveObservable: Observable<NotePositionDelta>
): BaseEditorUseCase() {

    override fun start(editor: Editor, noteRepository: NoteRepository) {
        val noteObservable = noteMoveObservable
            .switchMap { (noteId, _) -> editor.getNoteById(noteId) }
        val deltaObservable = noteMoveObservable.map { (_, delta) -> delta }

        deltaObservable.withLatestFrom(editor.userSelectedNote, noteObservable) { delta, optSelectedNote, note ->
            doOnUserSelectedNote(optSelectedNote, note) {
                note.id to note.position + delta
            }
        }
            .mapOptional { it }
            .subscribe { (noteId, newPosition) ->
                val attribute = NoteAttribute.Pos(newPosition)
                noteRepository.updateNote(noteId, listOf(attribute))
            }
            .addTo(disposableBag)
    }
}