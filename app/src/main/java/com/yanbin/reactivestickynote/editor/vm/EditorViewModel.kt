package com.yanbin.reactivestickynote.editor.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.reactivestickynote.stickynote.model.StickyNote
import com.yanbin.reactivestickynote.editor.usecase.*
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class EditorViewModel(
    private val editor: Editor,
    private val noteRepository: NoteRepository
): ViewModel() {

    private val tapCanvasFlow = MutableSharedFlow<Unit>()
    private val tapCreateFlow = MutableSharedFlow<Unit>()

    val openEditTextScreen: SharedFlow<StickyNote> = editor.openEditTextScreen
    val showContextMenu = editor.isContextMenuShown
    val showAddButton = editor.isAddButtonShown

    init {
        ChangeColorUseCase().startFlow(editor, noteRepository).launchIn(viewModelScope)
        DeleteNoteUseCase().startFlow(editor, noteRepository).launchIn(viewModelScope)
        EditTextUseCase().startFlow(editor).launchIn(viewModelScope)
        TapCanvasUseCae(tapCanvasFlow).startFlow(editor).launchIn(viewModelScope)
        AddNoteUseCae(tapCreateFlow).startFlow(noteRepository).launchIn(viewModelScope)
    }

    fun addNewNote() = viewModelScope.launch {
        tapCreateFlow.emit(Unit)
    }

    fun tapCanvas() = viewModelScope.launch {
        tapCanvasFlow.emit(Unit)
    }

    override fun onCleared() {
        editor.onDestroy()
    }

}