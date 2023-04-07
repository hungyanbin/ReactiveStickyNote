package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.account.AccountService
import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.reactivestickynote.stickynote.model.SelectedNote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class TapNoteUseCae(
    private val accountService: AccountService,
    private val tapNoteFlow: Flow<String>
) {

    fun startFlow(editor: Editor): Flow<Any> {
        return tapNoteFlow
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
    }

    private fun isNoteSelecting(id: String, selectedNotes: List<SelectedNote>): Boolean {
        return selectedNotes.any { it.noteId == id }
    }

    private suspend fun isSelectedByUser(id: String, selectedNotes: List<SelectedNote>): Boolean {
        return selectedNotes.find { selectedNote -> selectedNote.userName == accountService.getCurrentAccount().userName }
            ?.let { selectedNote -> selectedNote.noteId == id } ?: false
    }
}