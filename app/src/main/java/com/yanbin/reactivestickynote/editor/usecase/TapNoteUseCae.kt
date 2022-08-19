package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.account.AccountService
import com.yanbin.reactivestickynote.editor.domain.StickyNoteEditor
import com.yanbin.reactivestickynote.stickynote.model.SelectedNote
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo

class TapNoteUseCae(
    private val accountService: AccountService,
    private val tapNoteObservable: Observable<String>
): BaseEditorUseCase() {

    override fun start(stickyNoteEditor: StickyNoteEditor) {
        tapNoteObservable.withLatestFrom(stickyNoteEditor.selectedNotes) { id, selectedNotes ->
                id to selectedNotes
            }.subscribe { (id, selectedNotes) ->
                if (isNoteSelecting(id, selectedNotes)) {
                    if (isSelectedByUser(id, selectedNotes)) {
                        stickyNoteEditor.setNoteUnSelected(id)
                        stickyNoteEditor.showAddButton()
                    } else {
                        // can not select other user's note
                    }
                } else {
                    stickyNoteEditor.setNoteSelected(id)
                    stickyNoteEditor.showContextMenu()
                }
            }
            .addTo(disposableBag)
    }

    private fun isNoteSelecting(id: String, selectedNotes: List<SelectedNote>): Boolean {
        return selectedNotes.any { it.noteId == id }
    }

    private fun isSelectedByUser(id: String, selectedNotes: List<SelectedNote>): Boolean {
        return selectedNotes.find { selectedNote -> selectedNote.userName == accountService.getCurrentAccount().userName }
            ?.let { selectedNote -> selectedNote.noteId == id } ?: false
    }
}