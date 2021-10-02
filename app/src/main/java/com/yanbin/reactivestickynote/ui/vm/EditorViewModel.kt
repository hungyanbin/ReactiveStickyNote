package com.yanbin.reactivestickynote.ui.vm

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.domain.CoEditor
import com.yanbin.reactivestickynote.model.StickyNote
import com.yanbin.reactivestickynote.model.Position
import io.reactivex.rxjava3.core.Observable
import java.util.*

class EditorViewModel(
    private val coEditor: CoEditor
): ViewModel() {

    val allVisibleNoteIds: Observable<List<String>> = coEditor.allVisibleNotes
    val selectingNote: Observable<Optional<StickyNote>> = coEditor.selectedNote
    val openEditTextScreen: Observable<String> = coEditor.openEditTextScreen
    val showContextMenu = coEditor.showContextMenu
    val showAddButton = coEditor.showAdderButton

    init {
        coEditor.start()
    }

    fun moveNote(noteId: String, positionDelta: Position) {
        coEditor.moveNote(noteId, positionDelta)
    }

    fun addNewNote() {
        coEditor.addNewNote()
    }

    fun tapNote(stickyNote: StickyNote) {
        coEditor.selectNote(stickyNote.id)
    }

    fun tapCanvas() {
        coEditor.clearSelection()
    }

    fun getNoteById(id: String) = coEditor.getNoteById(id)

    override fun onCleared() {
        coEditor.stop()
    }

}