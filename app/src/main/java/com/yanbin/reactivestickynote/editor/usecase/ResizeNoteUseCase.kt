package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.StickyNoteEditor
import com.yanbin.reactivestickynote.editor.model.StickyNote
import com.yanbin.reactivestickynote.editor.model.YBSize
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo

// Triple for noteId, widthDelta and heightDelta
typealias NoteSizeDelta = Triple<String, Float, Float>

class ResizeNoteUseCase(
    private val noteResizeObservable: Observable<NoteSizeDelta>
): BaseEditorUseCase() {

    override fun start(stickyNoteEditor: StickyNoteEditor) {
        val noteObservable = noteResizeObservable
            .switchMap { (noteId) -> stickyNoteEditor.getNoteById(noteId) }
        val deltaObservable = noteResizeObservable.map { (_, widthDelta, heightDelta) -> widthDelta to heightDelta }

        deltaObservable.withLatestFrom(stickyNoteEditor.userSelectedNote, noteObservable) { (widthDelta, heightDelta), optSelectedNote, note ->
            doOnUserSelectedNote(optSelectedNote, note) {
                changeNoteSizeWithConstraint(note, widthDelta, heightDelta)
            }
        }
            .mapOptional { it }
            .subscribe { note ->
                stickyNoteEditor.updateNote(note)
            }
            .addTo(disposableBag)
    }

    private fun changeNoteSizeWithConstraint(note: StickyNote, widthDelta: Float, heightDelta: Float): StickyNote {
        val currentSize = note.size
        val newWidth = (currentSize.width + widthDelta).coerceAtLeast(StickyNote.MIN_SIZE)
        val newHeight = (currentSize.height + heightDelta).coerceAtLeast(StickyNote.MIN_SIZE)
        return note.copy(size = YBSize(newWidth, newHeight))
    }
}