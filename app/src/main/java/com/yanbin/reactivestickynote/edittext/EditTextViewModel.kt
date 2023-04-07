package com.yanbin.reactivestickynote.edittext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.reactivestickynote.stickynote.model.NoteAttribute
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EditTextViewModel(
    private val noteRepository: NoteRepository,
    private val noteId: String,
    defaultText: String
) : ViewModel() {

    private val disposableBag = CompositeDisposable()

    private val _textFlow = MutableStateFlow(defaultText)
    val textFlow: StateFlow<String> = _textFlow

    private val _leavePageFlow = MutableSharedFlow<Unit>()
    val leavePageFlow: SharedFlow<Unit> = _leavePageFlow

    fun onTextChanged(newText: String) {
        _textFlow.tryEmit(newText)
    }

    fun onConfirmClicked() = viewModelScope.launch {
        val note = noteRepository.getNoteById(noteId).first()
        val text = textFlow.value

        val attribute = NoteAttribute.Text(text)
        noteRepository.updateNote(note.id, listOf(attribute))
        _leavePageFlow.emit(Unit)
    }

    fun onCancelClicked() = viewModelScope.launch {
        _leavePageFlow.emit(Unit)
    }

    override fun onCleared() {
        disposableBag.clear()
    }
}