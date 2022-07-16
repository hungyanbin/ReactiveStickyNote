package com.yanbin.reactivestickynote.domain

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.data.NoteRepository
import com.yanbin.reactivestickynote.model.Note
import com.yanbin.reactivestickynote.model.Position
import com.yanbin.reactivestickynote.model.YBColor
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.Observables
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*

class EditorViewModel(
    private val noteRepository: NoteRepository
): ViewModel() {

    private val disposableBag = CompositeDisposable()
    private val selectingNoteIdSubject = BehaviorSubject.createDefault("")
    private val openEditTextSubject = PublishSubject.create<Note>()

    val allNotes: Observable<List<Note>> = noteRepository.getAllNotes()
    val selectingNote: Observable<Optional<Note>> = Observables.combineLatest(allNotes, selectingNoteIdSubject) { notes, id ->
        Optional.ofNullable<Note>(notes.find { note -> note.id == id })
    }.replay(1).autoConnect()

    val selectingColor: Observable<YBColor> = selectingNote
        .mapOptional { it }
        .map { it.color }
    val openEditTextScreen: Observable<Note> = openEditTextSubject.hide()

    fun moveNote(noteId: String, positionDelta: Position) {
        Observable.just(Pair(noteId, positionDelta))
            .withLatestFrom(allNotes) { (noteId, positionDelta), notes ->
                val currentNote = notes.find { note -> note.id == noteId }
                Optional.ofNullable(currentNote?.copy(position = currentNote.position + positionDelta))
            }
            .mapOptional { it }
            .subscribe { note ->
                noteRepository.putNote(note)
            }
            .addTo(disposableBag)
    }

    fun addNewNote() {
        val newNote = Note.createRandomNote()
        noteRepository.createNote(newNote)
    }

    fun tapNote(note: Note) {
        val selectingNoteId = selectingNoteIdSubject.value
        if (selectingNoteId == note.id) {
            selectingNoteIdSubject.onNext("")
        } else {
            selectingNoteIdSubject.onNext(note.id)
        }
    }

    fun tapCanvas() {
        selectingNoteIdSubject.onNext("")
    }

    fun onDeleteClicked() {
        val selectingNoteId = selectingNoteIdSubject.value
        if (selectingNoteId.isNotEmpty()) {
            noteRepository.deleteNote(selectingNoteId)
            selectingNoteIdSubject.onNext("")
        }
    }

    fun onColorSelected(color: YBColor) {
        runOnSelectingNote { note ->
            val newNote = note.copy(color = color)
            noteRepository.putNote(newNote)
        }
    }

    fun onEditTextClicked() {
        runOnSelectingNote { note ->
            openEditTextSubject.onNext(note)
        }
    }

    override fun onCleared() {
        disposableBag.clear()
    }

    private fun runOnSelectingNote(runner: (Note) -> Unit) {
        selectingNote
            .take(1)
            .mapOptional { it }
            .subscribe(runner)
            .addTo(disposableBag)
    }
}