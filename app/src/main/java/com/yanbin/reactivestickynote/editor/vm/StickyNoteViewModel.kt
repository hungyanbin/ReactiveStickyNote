package com.yanbin.reactivestickynote.editor.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanbin.reactivestickynote.account.AccountService
import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.reactivestickynote.editor.usecase.*
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.reactivestickynote.stickynote.model.Position
import com.yanbin.utils.fold
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StickyNoteViewModel(
    private val editor: Editor,
    private val noteRepository: NoteRepository,
    private val accountService: AccountService
): ViewModel() {

    private val moveNoteFlow = MutableSharedFlow<NotePositionDelta>()
    private val resizeNoteFlow = MutableSharedFlow<NoteSizeDelta>()
    private val tapNoteFlow = MutableSharedFlow<String>()

    init {
        MoveNoteUseCase(moveNoteFlow).startFlow(editor, noteRepository).launchIn(viewModelScope)
        ResizeNoteUseCase(resizeNoteFlow).startFlow(editor, noteRepository).launchIn(viewModelScope)
        TapNoteUseCae(accountService, tapNoteFlow).startFlow(editor).launchIn(viewModelScope)
    }

    fun moveNote(noteId: String, positionDelta: Position) = viewModelScope.launch {
        moveNoteFlow.emit(noteId to positionDelta)
    }

    fun onChangeSize(noteId: String, widthDelta: Float, heightDelta: Float) = viewModelScope.launch {
        resizeNoteFlow.emit(Triple(noteId, widthDelta, heightDelta))
    }

    fun tapNote(id: String) = viewModelScope.launch {
        tapNoteFlow.emit(id)
    }

    fun getNoteById(id: String): Flow<StickyNoteUiModel> = combine(editor.getNoteById(id), editor.selectedNotes, editor.userSelectedNote)
         { note, selectedNotes, userSelectedNote ->
            val selectedNote = selectedNotes.find { it.noteId == note.id }
            if (selectedNote != null) {
                val isCurrentUser = userSelectedNote.fold(someFun = { it.noteId == note.id }, emptyFun = { false })
                StickyNoteUiModel(note, StickyNoteUiModel.State.Selected(selectedNote.userName, isLocked = !isCurrentUser))
            } else {
                StickyNoteUiModel(note, StickyNoteUiModel.State.Normal)
            }
        }
        .distinctUntilChanged()

}