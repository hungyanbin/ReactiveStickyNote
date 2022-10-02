package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.Editor
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo

class TapCanvasUseCae(
    private val tapCanvasObservable: Observable<Unit>
): BaseEditorUseCase() {

    override fun start(editor: Editor) {
        tapCanvasObservable.withLatestFrom(editor.userSelectedNote) { _, userSelectedNote -> userSelectedNote }
            .mapOptional { it }
            .subscribe { selectedNote ->
                editor.setNoteUnSelected(selectedNote.noteId)
                editor.showAddButton()
            }
            .addTo(disposableBag)
    }
}