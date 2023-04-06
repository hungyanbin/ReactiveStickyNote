package com.yanbin.reactivestickynote.editor.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.reactivestickynote.stickynote.model.StickyNote
import com.yanbin.reactivestickynote.editor.usecase.*
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.reactivestickynote.stickynote.data.OldNoteRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class EditorViewModel(
    private val editor: Editor,
    private val noteRepository: NoteRepository
): ViewModel() {

    private val tapCanvasSubject = PublishSubject.create<Unit>()
    private val tapCreateSubject = PublishSubject.create<Unit>()

    val openEditTextScreen: Observable<StickyNote> = editor.openEditTextScreen
    val showContextMenu = editor.isContextMenuShown
    val showAddButton = editor.isAddButtonShown

    private val useCases: MutableList<BaseEditorUseCase> = mutableListOf()

    init {
        DeleteNoteUseCase(viewModelScope).apply {
            start(editor, noteRepository)
            useCases.add(this)
        }
        ChangeColorUseCase(viewModelScope).apply {
            start(editor, noteRepository)
            useCases.add(this)
        }
        EditTextUseCase().apply {
            start(editor, noteRepository)
            useCases.add(this)
        }
        TapCanvasUseCae(tapCanvasSubject.hide(), viewModelScope).apply {
            start(editor, noteRepository)
            useCases.add(this)
        }
        AddNoteUseCae(tapCreateSubject, viewModelScope).apply {
            start(editor, noteRepository)
            useCases.add(this)
        }
    }

    fun addNewNote() {
        tapCreateSubject.onNext(Unit)
    }

    fun tapCanvas() {
        tapCanvasSubject.onNext(Unit)
    }

    override fun onCleared() {
        useCases.forEach { it.stop() }
    }

}