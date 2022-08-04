package com.yanbin.reactivestickynote.editor.vm

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.editor.data.NoteRepository
import com.yanbin.reactivestickynote.editor.domain.StickyNoteEditor
import com.yanbin.reactivestickynote.editor.model.Position
import com.yanbin.reactivestickynote.editor.model.StickyNote
import com.yanbin.reactivestickynote.editor.usecase.BaseRxJavaUseCase
import com.yanbin.reactivestickynote.editor.usecase.ChangeColorUseCase
import com.yanbin.reactivestickynote.editor.usecase.DeleteNoteUseCase
import com.yanbin.reactivestickynote.editor.usecase.EditTextUseCase
import io.reactivex.rxjava3.core.Observable

class EditorViewModel(
    private val stickyNoteEditor: StickyNoteEditor,
    private val noteRepository: NoteRepository
): ViewModel() {

    val openEditTextScreen: Observable<StickyNote> = stickyNoteEditor.openEditTextScreen
    val allVisibleNoteIds = stickyNoteEditor.allVisibleNoteIds
    val showContextMenu = stickyNoteEditor.showContextMenu
    val showAddButton = stickyNoteEditor.showAddButton

    val viewPortScale = stickyNoteEditor.viewPort.scale
    val viewPortCenter = stickyNoteEditor.viewPort.center

    private val useCases: MutableList<BaseRxJavaUseCase> = mutableListOf()

    init {
        DeleteNoteUseCase(noteRepository).apply {
            start(stickyNoteEditor)
            useCases.add(this)
        }
        ChangeColorUseCase(noteRepository).apply {
            start(stickyNoteEditor)
            useCases.add(this)
        }
        EditTextUseCase().apply {
            start(stickyNoteEditor)
            useCases.add(this)
        }
        stickyNoteEditor.start()
    }

    fun addNewNote() {
        stickyNoteEditor.addNewNote()
    }

    fun tapCanvas() {
        stickyNoteEditor.clearSelection()
    }

    fun transformViewPort(position: Position, scale: Float) {
        stickyNoteEditor.viewPort.transformDelta(position, scale)
    }

    override fun onCleared() {
        useCases.forEach { it.stop() }
        stickyNoteEditor.stop()
    }

}