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
import java.util.*

class EditorViewModel(
    private val noteRepository: NoteRepository
): ViewModel() {

    private val disposableBag = CompositeDisposable()
    private val selectingNoteIdSubject = BehaviorSubject.createDefault("")
    private val selectingNoteSubject = BehaviorSubject.createDefault(Optional.empty<Note>())

    val allNotes: Observable<List<Note>> = noteRepository.getAllNotes()
    val selectingNote: Observable<Optional<Note>> = selectingNoteSubject.hide()
    val selectingColor: Observable<YBColor> = selectingNote
        .mapOptional { it }
        .map { it.color }

    init {
        Observables.combineLatest(allNotes, selectingNoteIdSubject) { notes, id ->
            Optional.ofNullable<Note>(notes.find { note -> note.id == id })
        }.fromComputation()
            .subscribe { optNote ->
                selectingNoteSubject.onNext(optNote)
            }
            .addTo(disposableBag)
    }

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
        noteRepository.addNote(newNote)
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
        val optSelectingNote = selectingNoteSubject.value

        optSelectingNote
            .map { note -> note.copy(color = color) }
            .ifPresent { note ->
                noteRepository.putNote(note)
            }
    }

    override fun onCleared() {
        disposableBag.clear()
    }

}