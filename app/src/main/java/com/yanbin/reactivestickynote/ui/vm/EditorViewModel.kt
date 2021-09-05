package com.yanbin.reactivestickynote.ui.vm

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.domain.NoteEditor
import com.yanbin.reactivestickynote.model.Note
import com.yanbin.reactivestickynote.model.Position
import io.reactivex.rxjava3.core.Observable
import java.util.*

class EditorViewModel(
    private val noteEditor: NoteEditor
): ViewModel() {

    val selectingNote: Observable<Optional<Note>> = noteEditor.selectedNote
    val openEditTextScreen: Observable<Note> = noteEditor.openEditTextScreen
    val allVisibleNoteIds = noteEditor.allVisibleNotes
    val showContextMenu = noteEditor.showContextMenu
    val showAddButton = noteEditor.showAddButton


    init {
        noteEditor.start()
    }

    fun moveNote(noteId: String, positionDelta: Position) {
        noteEditor.moveNote(noteId, positionDelta)
    }

    fun addNewNote() {
        noteEditor.addNewNote()
    }

    fun tapNote(note: Note) {
        noteEditor.selectNote(note.id)
    }

    fun tapCanvas() {
        noteEditor.clearSelection()
    }

    fun getNoteById(id: String) = noteEditor.getNoteById(id)

    override fun onCleared() {
        noteEditor.stop()
    }

}