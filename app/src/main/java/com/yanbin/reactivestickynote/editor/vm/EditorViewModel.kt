package com.yanbin.reactivestickynote.editor.vm

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.editor.domain.StickyNoteEditor
import com.yanbin.reactivestickynote.editor.model.StickyNote
import com.yanbin.reactivestickynote.editor.usecase.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class EditorViewModel(
    private val stickyNoteEditor: StickyNoteEditor
): ViewModel() {

    private val tapCanvasSubject = PublishSubject.create<Unit>()

    val openEditTextScreen: Observable<StickyNote> = stickyNoteEditor.openEditTextScreen
    val showContextMenu = stickyNoteEditor.isContextMenuShown
    val showAddButton = stickyNoteEditor.isAddButtonShown

    private val useCases: MutableList<BaseEditorUseCase> = mutableListOf()

    init {
        DeleteNoteUseCase().apply {
            start(stickyNoteEditor)
            useCases.add(this)
        }
        ChangeColorUseCase().apply {
            start(stickyNoteEditor)
            useCases.add(this)
        }
        EditTextUseCase().apply {
            start(stickyNoteEditor)
            useCases.add(this)
        }
        TapCanvasUseCae(tapCanvasSubject.hide()).apply {
            start(stickyNoteEditor)
            useCases.add(this)
        }
    }

    fun addNewNote() {
        stickyNoteEditor.addNewNote()
    }

    fun tapCanvas() {
        tapCanvasSubject.onNext(Unit)
    }

    override fun onCleared() {
        useCases.forEach { it.stop() }
    }

}