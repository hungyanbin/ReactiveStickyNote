package com.yanbin.reactivestickynote.domain

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.data.NoteRepository
import com.yanbin.reactivestickynote.model.Note
import com.yanbin.reactivestickynote.model.Position
import com.yanbin.reactivestickynote.model.YBColor
import com.yanbin.utils.fromComputation
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.Observables
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*

class EditorViewModel(
    private val noteEditor: NoteEditor
): ViewModel() {

    val allVisibleNoteIds: Observable<List<String>> = noteEditor.allVisibleNotes
    val selectingNote: Observable<Optional<Note>> = noteEditor.selectedNote
    val openEditTextScreen: Observable<String> = noteEditor.openEditTextScreen
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