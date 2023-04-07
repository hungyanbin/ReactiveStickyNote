package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.account.AccountService
import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.reactivestickynote.stickynote.data.OldNoteRepository
import com.yanbin.reactivestickynote.stickynote.model.SelectedNote
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx3.asFlow

class TapNoteUseCae(
    private val accountService: AccountService,
    private val tapNoteObservable: Observable<String>,
    private val scope: CoroutineScope
): BaseEditorUseCase() {

    override fun start(editor: Editor, noteRepository: NoteRepository) {
        tapNoteObservable.asFlow()
            .map { id -> id to editor.selectedNotes.first() }
            .onEach { (id, selectedNotes) ->
                if (isNoteSelecting(id, selectedNotes)) {
                    if (isSelectedByUser(id, selectedNotes)) {
                        editor.setNoteUnSelected(id)
                        editor.showAddButton()
                    } else {
                        // can not select other user's note
                    }
                } else {
                    editor.setNoteSelected(id)
                    editor.showContextMenu()
                }
            }
            .launchIn(scope)
    }

    private fun isNoteSelecting(id: String, selectedNotes: List<SelectedNote>): Boolean {
        return selectedNotes.any { it.noteId == id }
    }

    private suspend fun isSelectedByUser(id: String, selectedNotes: List<SelectedNote>): Boolean {
        return selectedNotes.find { selectedNote -> selectedNote.userName == accountService.getCurrentAccount().userName }
            ?.let { selectedNote -> selectedNote.noteId == id } ?: false
    }
}