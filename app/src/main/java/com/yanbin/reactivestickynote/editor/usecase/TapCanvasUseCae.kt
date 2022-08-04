package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.StickyNoteEditor
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.*

class TapCanvasUseCae(
    private val tapCanvasObservable: Observable<Unit>
): BaseEditorUseCase() {

    override fun start(stickyNoteEditor: StickyNoteEditor) {
        tapCanvasObservable.withLatestFrom(stickyNoteEditor.userSelectedNote) { _, userSelectedNote -> userSelectedNote }
            .mapOptional { it }
            .subscribe { selectedNote ->
                stickyNoteEditor.setNoteUnSelected(selectedNote.noteId)
                stickyNoteEditor.showAddButton()
            }
            .addTo(disposableBag)
    }
}