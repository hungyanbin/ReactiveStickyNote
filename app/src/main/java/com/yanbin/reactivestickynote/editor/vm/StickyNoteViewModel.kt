package com.yanbin.reactivestickynote.editor.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanbin.reactivestickynote.account.AccountService
import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.reactivestickynote.stickynote.model.Position
import com.yanbin.reactivestickynote.editor.usecase.*
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.utils.fold
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.Observables
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.rx3.asObservable

class StickyNoteViewModel(
    private val editor: Editor,
    private val noteRepository: NoteRepository,
    private val accountService: AccountService
): ViewModel() {

    private val moveNoteSubject = PublishSubject.create<NotePositionDelta>()
    private val resizeNoteSubject = PublishSubject.create<NoteSizeDelta>()
    private val tapNoteSubject = PublishSubject.create<String>()

    init {
        MoveNoteUseCase(moveNoteSubject.hide()).startFlow(editor, noteRepository).launchIn(viewModelScope)
        ResizeNoteUseCase(resizeNoteSubject.hide()).startFlow(editor, noteRepository).launchIn(viewModelScope)
        TapNoteUseCae(accountService, tapNoteSubject.hide()).startFlow(editor).launchIn(viewModelScope)
    }

    fun moveNote(noteId: String, positionDelta: Position) {
        moveNoteSubject.onNext(noteId to positionDelta)
    }

    fun onChangeSize(noteId: String, widthDelta: Float, heightDelta: Float) {
        resizeNoteSubject.onNext(Triple(noteId, widthDelta, heightDelta))
    }

    fun tapNote(id: String) {
        tapNoteSubject.onNext(id)
    }

    fun getNoteById(id: String): Observable<StickyNoteUiModel> = Observables.combineLatest(editor.getNoteById(id).asObservable(), editor.selectedNotes.asObservable(), editor.userSelectedNote.asObservable())
        .map { (note, selectedNotes, userSelectedNote) ->
            val selectedNote = selectedNotes.find { it.noteId == note.id }
            if (selectedNote != null) {
                val isCurrentUser = userSelectedNote.fold(someFun = { it.noteId == note.id }, emptyFun = { false })
                StickyNoteUiModel(note, StickyNoteUiModel.State.Selected(selectedNote.userName, isLocked = !isCurrentUser))
            } else {
                StickyNoteUiModel(note, StickyNoteUiModel.State.Normal)
            }
        }
        .distinctUntilChanged()
}