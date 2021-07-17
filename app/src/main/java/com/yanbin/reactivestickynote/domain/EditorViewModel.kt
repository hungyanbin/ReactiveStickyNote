package com.yanbin.reactivestickynote.domain

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.data.NoteRepository
import com.yanbin.reactivestickynote.model.Note
import com.yanbin.reactivestickynote.model.Position
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.*

class EditorViewModel(
    private val noteRepository: NoteRepository
): ViewModel() {

    private val disposableBag = CompositeDisposable()

    val allNotes: Observable<List<Note>> = noteRepository.getAllNotes()

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

    override fun onCleared() {
        disposableBag.clear()
    }

}