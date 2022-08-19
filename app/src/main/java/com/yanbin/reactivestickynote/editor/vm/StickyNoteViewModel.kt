package com.yanbin.reactivestickynote.editor.vm

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.account.AccountService
import com.yanbin.reactivestickynote.editor.domain.StickyNoteEditor
import com.yanbin.reactivestickynote.stickynote.model.Position
import com.yanbin.reactivestickynote.editor.usecase.*
import com.yanbin.utils.fold
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.Observables
import io.reactivex.rxjava3.subjects.PublishSubject

class StickyNoteViewModel(
    private val stickyNoteEditor: StickyNoteEditor,
    private val accountService: AccountService
): ViewModel() {

    private val moveNoteSubject = PublishSubject.create<NotePositionDelta>()
    private val resizeNoteSubject = PublishSubject.create<NoteSizeDelta>()
    private val tapNoteSubject = PublishSubject.create<String>()
    private val useCases = mutableListOf<BaseEditorUseCase>()

    init {
        MoveNoteUseCase(moveNoteSubject.hide()).apply {
            start(stickyNoteEditor)
            useCases.add(this)
        }
        ResizeNoteUseCase(resizeNoteSubject.hide()).apply {
            start(stickyNoteEditor)
            useCases.add(this)
        }
        TapNoteUseCae(accountService, tapNoteSubject.hide()).apply {
            start(stickyNoteEditor)
            useCases.add(this)
        }
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

    fun getNoteById(id: String): Observable<StickyNoteUiModel> = Observables.combineLatest(stickyNoteEditor.getNoteById(id), stickyNoteEditor.selectedNotes, stickyNoteEditor.userSelectedNote)
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

    override fun onCleared() {
        useCases.forEach { it.stop() }
    }
}