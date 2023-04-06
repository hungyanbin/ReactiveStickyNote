package com.yanbin.reactivestickynote.edittext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.reactivestickynote.stickynote.model.NoteAttribute
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx3.asObservable

class EditTextViewModel(
    private val noteRepository: NoteRepository,
    private val noteId: String,
    defaultText: String
) : ViewModel() {

    private val disposableBag = CompositeDisposable()

    private val textSubject = BehaviorSubject.createDefault(defaultText)

    val text: Observable<String> = textSubject.hide()

    fun onTextChanged(newText: String) {
        textSubject.onNext(newText)
    }

    private val leavePageSubject = PublishSubject.create<Unit>()
    val leavePage: Observable<Unit> = leavePageSubject.hide()

    fun onConfirmClicked() {
        noteRepository.getNoteById(noteId).asObservable()
            .withLatestFrom(text) { note, text ->
                note.id to text
            }
            .firstElement()
            .subscribe { (id, text) ->
                viewModelScope.launch {
                    val attribute = NoteAttribute.Text(text)
                    noteRepository.updateNote(id, listOf(attribute))
                    leavePageSubject.onNext(Unit)
                }
            }
            .addTo(disposableBag)
    }

    fun onCancelClicked() {
        leavePageSubject.onNext(Unit)
    }

    override fun onCleared() {
        disposableBag.clear()
    }
}