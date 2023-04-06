package com.yanbin.reactivestickynote.editor.domain

import com.yanbin.reactivestickynote.account.AccountService
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.reactivestickynote.stickynote.model.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.rx3.asObservable
import java.util.*

class Editor(
    private val noteRepository: NoteRepository,
    private val accountService: AccountService
) {

    private val _showContextMenu = BehaviorSubject.createDefault(false)
    private val _showAddButton = BehaviorSubject.createDefault(true)
    private val openEditTextScreenSignal = PublishSubject.create<StickyNote>()

    val selectedNotes: Observable<List<SelectedNote>> = noteRepository.getAllSelectedNotes().asObservable()
    val isContextMenuShown: Observable<Boolean> = _showContextMenu.hide()
    val isAddButtonShown: Observable<Boolean> = _showAddButton.hide()

    val userSelectedNote: Observable<Optional<SelectedNote>> = selectedNotes.map { notes ->
        Optional.ofNullable(notes.find { note -> note.userName == accountService.getCurrentAccount().userName })
    }.startWithItem(Optional.empty<SelectedNote>())

    // State
    val selectedStickyNote: Observable<Optional<StickyNote>> = userSelectedNote
        .switchMap { optSelectedNote ->
            if (optSelectedNote.isPresent) {
                getNoteById(optSelectedNote.get().noteId)
                    .map { Optional.ofNullable(it) }
            } else {
                Observable.just(Optional.empty())
            }
        }
    val openEditTextScreen: Observable<StickyNote> = openEditTextScreenSignal.hide()

    // Component
    val contextMenu = ContextMenu(selectedStickyNote)
    val viewPort = ViewPort(noteRepository)

    suspend fun setNoteSelected(id: String) {
        noteRepository.setNoteSelection(id, accountService.getCurrentAccount())
    }

    suspend fun setNoteUnSelected(id: String) {
        noteRepository.removeNoteSelection(id, accountService.getCurrentAccount())
    }

    fun showAddButton() {
        _showAddButton.onNext(true)
        _showContextMenu.onNext(false)
    }

    fun showContextMenu() {
        _showAddButton.onNext(false)
        _showContextMenu.onNext(true)
    }

    fun getNoteById(id: String): Observable<StickyNote> {
        return noteRepository.getNoteById(id).asObservable()
    }

    fun navigateToEditTextPage(stickyNote: StickyNote) {
        openEditTextScreenSignal.onNext(stickyNote)
    }

}
