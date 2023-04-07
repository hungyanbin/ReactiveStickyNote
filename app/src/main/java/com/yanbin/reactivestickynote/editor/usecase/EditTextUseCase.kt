package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.ContextMenuEvent
import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.reactivestickynote.stickynote.data.OldNoteRepository
import com.yanbin.utils.filterInstance
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.rx3.asObservable

class EditTextUseCase : BaseEditorUseCase() {

    override fun start(editor: Editor, noteRepository: NoteRepository) {
        editor.contextMenu
            .contextMenuEvents
            .asObservable()
            .filterInstance<ContextMenuEvent.NavigateToEditTextPage>()
            .withLatestFrom(editor.selectedStickyNote) { _, selectedNote -> selectedNote}
            .mapOptional { it }
            .subscribe { stickyNote ->
                editor.navigateToEditTextPage(stickyNote)
            }
            .addTo(disposableBag)
    }
}