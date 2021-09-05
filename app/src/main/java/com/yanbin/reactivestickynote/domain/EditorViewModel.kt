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

    private val disposableBag = CompositeDisposable()
    private val selectingNoteSubject = BehaviorSubject.createDefault(Optional.empty<Note>())

    val allNotes = noteEditor.allNotes
    val selectingNote: Observable<Optional<Note>> = noteEditor.selectedNote
    val selectingColor: Observable<YBColor> = selectingNote
        .mapOptional { it }
        .map { it.color }
    val openEditTextScreen: Observable<Note> = noteEditor.openEditTextScreen

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

    fun onDeleteClicked() {
        noteEditor.contextMenu.onDeleteClicked()
    }

    fun onColorSelected(color: YBColor) {
        noteEditor.contextMenu.onColorSelected(color)
    }

    fun onEditTextClicked() {
        noteEditor.contextMenu.onEditTextClicked()
    }

    override fun onCleared() {
        noteEditor.stop()
        disposableBag.clear()
    }

}