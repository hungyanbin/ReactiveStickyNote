package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.ContextMenuEvent
import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.utils.filterInstance
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DeleteNoteUseCase(
    private val scope: CoroutineScope
) : BaseEditorUseCase() {

    override fun start(editor: Editor, noteRepository: NoteRepository) {
        editor.contextMenu
            .contextMenuEvents
            .filterInstance<ContextMenuEvent.DeleteNote>()
            .withLatestFrom(editor.userSelectedNote) { _, optSelectedNote ->
                optSelectedNote.map { note ->
                    note.noteId
                }
            }.mapOptional { it }
            .subscribe { id ->
                scope.launch {
                    editor.setNoteUnSelected(id)
                    noteRepository.deleteNote(id)
                    editor.showAddButton()
                }
            }
            .addTo(disposableBag)
    }
}