package com.yanbin.reactivestickynote.domain

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.data.NoteRepository
import com.yanbin.reactivestickynote.model.StickyNote
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

class EditTextViewModel(
    private val noteRepository: NoteRepository,
    noteId: String
) : ViewModel() {

    private val disposableBag = CompositeDisposable()

    private val noteSubject = BehaviorSubject.create<StickyNote>()
    private val textSubject = BehaviorSubject.createDefault("")
    private val leavePageSubject = PublishSubject.create<Unit>()

    val text: Observable<String> = textSubject.hide()
    val leavePage: Observable<Unit> = leavePageSubject.hide()

    init {
        noteRepository.getNoteById(noteId)
            .firstElement()
            .observeOn(Schedulers.io())
            .subscribe { note ->
                noteSubject.onNext(note)
                textSubject.onNext(note.text)
            }
            .addTo(disposableBag)
    }

    fun onTextChanged(newText: String) {
        textSubject.onNext(newText)
    }

    fun onConfirmClicked() {
        noteSubject.withLatestFrom(textSubject) { note, text ->
            note.copy(text = text)
        }
            .subscribe { newNote ->
                noteRepository.putNote(stickyNote = newNote)
                leavePageSubject.onNext(Unit)
            }
            .addTo(disposableBag)
    }

    fun onCancelClicked() {
        leavePageSubject.onNext(Unit)
    }
}