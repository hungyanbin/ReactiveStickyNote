package com.yanbin.reactivestickynote.ui.vm

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.domain.StickyNoteEditor
import com.yanbin.reactivestickynote.model.Note
import com.yanbin.reactivestickynote.model.Position
import io.reactivex.rxjava3.core.Observable
import java.util.*

class EditorViewModel(
    private val stickyNoteEditor: StickyNoteEditor
): ViewModel() {

    val selectingNote: Observable<Optional<Note>> = stickyNoteEditor.selectedNote
    val openEditTextScreen: Observable<Note> = stickyNoteEditor.openEditTextScreen
    val allVisibleNoteIds = stickyNoteEditor.allVisibleNotes
    val showContextMenu = stickyNoteEditor.showContextMenu
    val showAddButton = stickyNoteEditor.showAddButton

    init {
        stickyNoteEditor.start()
    }

    fun moveNote(noteId: String, positionDelta: Position) {
        stickyNoteEditor.moveNote(noteId, positionDelta)
    }

    fun addNewNote() {
        stickyNoteEditor.addNewNote()
    }

    fun tapNote(note: Note) {
        stickyNoteEditor.selectNote(note.id)
    }

    fun tapCanvas() {
        stickyNoteEditor.clearSelection()
    }

    fun getNoteById(id: String) = stickyNoteEditor.getNoteById(id)

    override fun onCleared() {
        stickyNoteEditor.stop()
    }

}