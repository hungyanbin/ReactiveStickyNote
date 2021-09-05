package com.yanbin.reactivestickynote.domain

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

class NoteEditor(
    private val noteRepository: NoteRepository
) {

    private val _showContextMenu = BehaviorSubject.createDefault(false)
    private val _showAddButton = BehaviorSubject.createDefault(true)
    private val openEditTextScreenSignal = PublishSubject.create<Unit>()
    private val selectedNoteId = BehaviorSubject.createDefault(Optional.empty<String>())

    // State
    val selectedNote: Observable<Optional<Note>> = selectedNoteId
        .flatMap { optId ->
            if (optId.isPresent) {
                noteRepository.getNoteById(optId.get())
                    .map { Optional.ofNullable(it) }
            } else {
                Observable.just(Optional.empty())
            }
        }

    val allVisibleNotes: Observable<List<String>> = noteRepository.getAllVisibleNoteIds()
    val showContextMenu: Observable<Boolean> = _showContextMenu.hide()
    val showAddButton: Observable<Boolean> = _showAddButton.hide()
    val openEditTextScreen: Observable<Note> = openEditTextScreenSignal.withLatestFrom(selectedNote) { _, optNote ->
        optNote
    }.mapOptional { it }

    // Component
    val contextMenu = ContextMenu(selectedNote)

    private val disposableBag = CompositeDisposable()

    fun start() {
        contextMenu.contextMenuEvents
            .subscribe { menuEvent ->
                when(menuEvent) {
                    ContextMenuEvent.NavigateToEditTextPage -> navigateToEditTextPage()
                    is ContextMenuEvent.ChangeColor -> changeColor(menuEvent.color)
                    ContextMenuEvent.DeleteNote -> deleteNote()
                }
            }
            .addTo(disposableBag)
    }

    fun stop() {
        disposableBag.clear()
    }

    fun selectNote(noteId: String) {
        selectedNoteId.onNext(Optional.of(noteId))
    }

    fun clearSelection() {
        selectedNoteId.onNext(Optional.empty())
        _showAddButton.onNext(true)
        _showContextMenu.onNext(false)
    }

    fun getNoteById(id: String): Observable<Note> {
        return noteRepository.getNoteById(id)
    }

    fun addNewNote() {
        val newNote = Note.createRandomNote()
        noteRepository.createNote(newNote)
    }

    fun moveNote(noteId: String, positionDelta: Position) {
        Observable.just(positionDelta)
            .withLatestFrom(noteRepository.getNoteById(noteId)) { delta, note ->
                note.copy(position = note.position + delta)
            }
            .subscribe { note ->
                noteRepository.putNote(note)
            }
            .addTo(disposableBag)
    }

    private fun navigateToEditTextPage() {
        openEditTextScreenSignal.onNext(Unit)
    }

    private fun deleteNote() {
        selectedNoteId.value?.ifPresent { id ->
            noteRepository.deleteNote(id)
            clearSelection()
        }
    }

    private fun changeColor(color: YBColor) {
        Observable.just(color)
            .withLatestFrom(selectedNote) { color, optSelectedNote ->
                optSelectedNote.map { note ->
                    note.copy(color = color)
                }
            }.mapOptional { it }
            .subscribe { newNote ->
                noteRepository.putNote(newNote)
            }
            .addTo(disposableBag)
    }

}
