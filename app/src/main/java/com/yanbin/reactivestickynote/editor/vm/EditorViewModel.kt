package com.yanbin.reactivestickynote.editor.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.reactivestickynote.stickynote.model.StickyNote
import com.yanbin.reactivestickynote.editor.usecase.*
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class EditorViewModel(
    private val editor: Editor,
    private val noteRepository: NoteRepository
): ViewModel() {

    private val tapCanvasSubject = PublishSubject.create<Unit>()
    private val tapCreateSubject = PublishSubject.create<Unit>()

    val openEditTextScreen: SharedFlow<StickyNote> = editor.openEditTextScreen
    val showContextMenu = editor.isContextMenuShown
    val showAddButton = editor.isAddButtonShown

    init {
        ChangeColorUseCase().startFlow(editor, noteRepository).launchIn(viewModelScope)
        DeleteNoteUseCase().startFlow(editor, noteRepository).launchIn(viewModelScope)
        EditTextUseCase().startFlow(editor).launchIn(viewModelScope)
        TapCanvasUseCae(tapCanvasSubject.hide()).startFlow(editor).launchIn(viewModelScope)
        AddNoteUseCae(tapCreateSubject.hide()).startFlow(noteRepository).launchIn(viewModelScope)
    }

    fun addNewNote() {
        tapCreateSubject.onNext(Unit)
    }

    fun tapCanvas() {
        tapCanvasSubject.onNext(Unit)
    }

    override fun onCleared() {
        editor.onDestroy()
    }

}