package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.common.YBSize
import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.reactivestickynote.stickynote.model.NoteAttribute
import com.yanbin.reactivestickynote.stickynote.model.StickyNote
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo

// Triple for noteId, widthDelta and heightDelta
typealias NoteSizeDelta = Triple<String, Float, Float>

class ResizeNoteUseCase(
    private val noteResizeObservable: Observable<NoteSizeDelta>
): BaseEditorUseCase() {

    override fun start(editor: Editor, noteRepository: NoteRepository) {
        val noteObservable = noteResizeObservable
            .switchMap { (noteId) -> editor.getNoteById(noteId) }
        val deltaObservable = noteResizeObservable.map { (_, widthDelta, heightDelta) -> widthDelta to heightDelta }

        deltaObservable.withLatestFrom(editor.userSelectedNote, noteObservable) { (widthDelta, heightDelta), optSelectedNote, note ->
            doOnUserSelectedNote(optSelectedNote, note) {
                note.id to changeNoteSizeWithConstraint(note, widthDelta, heightDelta)
            }
        }
            .mapOptional { it }
            .subscribe { (id, size) ->
                val attribute = NoteAttribute.Size(size)
                noteRepository.updateNote(id, listOf(attribute))
            }
            .addTo(disposableBag)
    }

    private fun changeNoteSizeWithConstraint(note: StickyNote, widthDelta: Float, heightDelta: Float): YBSize {
        val currentSize = note.size
        val newWidth = (currentSize.width + widthDelta).coerceAtLeast(StickyNote.MIN_SIZE)
        val newHeight = (currentSize.height + heightDelta).coerceAtLeast(StickyNote.MIN_SIZE)
        return YBSize(newWidth, newHeight)
    }
}