package com.yanbin.reactivestickynote.editor.domain

import com.yanbin.reactivestickynote.account.AccountService
import com.yanbin.reactivestickynote.editor.data.NoteRepository
import com.yanbin.reactivestickynote.editor.model.StickyNote
import com.yanbin.reactivestickynote.editor.model.Position
import com.yanbin.reactivestickynote.editor.model.SelectedNote
import com.yanbin.reactivestickynote.editor.model.YBColor
import com.yanbin.utils.fold
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*

class StickyNoteEditor(
    private val noteRepository: NoteRepository,
    private val accountService: AccountService
) {

    private val _showContextMenu = BehaviorSubject.createDefault(false)
    private val _showAddButton = BehaviorSubject.createDefault(true)
    private val openEditTextScreenSignal = PublishSubject.create<Unit>()

    val selectedNotes: Observable<List<SelectedNote>> = noteRepository.getAllSelectedNotes()
    val allVisibleNoteIds: Observable<List<String>> = noteRepository.getAllVisibleNoteIds()
    val showContextMenu: Observable<Boolean> = _showContextMenu.hide()
    val showAddButton: Observable<Boolean> = _showAddButton.hide()

    val userSelectedNote: Observable<Optional<SelectedNote>> = selectedNotes.map { notes ->
        Optional.ofNullable(notes.find { note -> note.userName == accountService.getCurrentAccount().userName })
    }

    // State
    val selectedNote: Observable<Optional<StickyNote>> = userSelectedNote
        .switchMap { optSelectedNote ->
            if (optSelectedNote.isPresent) {
                noteRepository.getNoteById(optSelectedNote.get().noteId)
                    .map { Optional.ofNullable(it) }
            } else {
                Observable.just(Optional.empty())
            }
        }
    val openEditTextScreen: Observable<StickyNote> = openEditTextScreenSignal.withLatestFrom(selectedNote) { _, optNote ->
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
        Observable.just(noteId)
            .withLatestFrom(userSelectedNote) { id, selectedNote ->
                id to selectedNote
            }
            .firstElement()
            .subscribe { (id, selectedNote) ->
                selectedNote.fold(
                    someFun = { note ->
                        if (note.noteId == id) {
                            setNoteUnSelected(id)
                            showAddButton()
                        } else {
                            setNoteUnSelected(note.noteId)
                            setNoteSelected(id)
                            showContextMenu()
                        }
                    },
                    emptyFun = {
                        setNoteSelected(id)
                        showContextMenu()
                    }
                )
            }
            .addTo(disposableBag)
    }

    private fun setNoteSelected(id: String) {
        noteRepository.addNoteSelection(id, accountService.getCurrentAccount().userName)
    }

    private fun setNoteUnSelected(id: String) {
        noteRepository.removeNoteSelection(id)
    }

    private fun showAddButton() {
        _showAddButton.onNext(true)
        _showContextMenu.onNext(false)
    }

    private fun showContextMenu() {
        _showAddButton.onNext(false)
        _showContextMenu.onNext(true)
    }

    fun clearSelection() {
        userSelectedNote
            .take(1)
            .mapOptional { it }
            .subscribe { selectedNote ->
                setNoteUnSelected(selectedNote.noteId)
                showAddButton()
            }
            .addTo(disposableBag)
    }

    fun getNoteById(id: String): Observable<StickyNote> {
        return noteRepository.getNoteById(id)
    }

    fun addNewNote() {
        val newNote = StickyNote.createRandomNote()
        noteRepository.createNote(newNote)
    }

    fun moveNote(noteId: String, positionDelta: Position) {
        Observable.just(positionDelta)
            .withLatestFrom(userSelectedNote, noteRepository.getNoteById(noteId)) { delta, optSelectedNote, note ->
                optSelectedNote.fold(
                    someFun = {
                        // Can move
                        if (it.noteId == noteId) {
                            note.copy(position = note.position + delta)
                        } else {
                            null
                        }
                    },
                    emptyFun = {
                        null
                    }
                ).let { Optional.ofNullable(it) }
            }
            .mapOptional { it }
            .subscribe { note ->
                noteRepository.putNote(note)
            }
            .addTo(disposableBag)
    }

    private fun navigateToEditTextPage() {
        openEditTextScreenSignal.onNext(Unit)
    }

    private fun deleteNote() {
        userSelectedNote.take(1)
            .mapOptional { it }
            .subscribe { selectedNote ->
                noteRepository.deleteNote(selectedNote.noteId)
                clearSelection()
            }
            .addTo(disposableBag)
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
