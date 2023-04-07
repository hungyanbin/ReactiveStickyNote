package com.yanbin.reactivestickynote.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanbin.reactivestickynote.account.AccountService
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val accountService: AccountService
): ViewModel() {

    private val disposableBag = CompositeDisposable()

    private val _textFlow = MutableStateFlow("")
    val textFlow: StateFlow<String> = _textFlow

    private val _toEditorPageFlow = MutableSharedFlow<Unit>()
    val toEditorPageFlow: SharedFlow<Unit> = _toEditorPageFlow

    fun onTextChanged(text: String) = viewModelScope.launch {
        _textFlow.emit(text)
    }

    fun onEnterClicked() = viewModelScope.launch {
        val name = textFlow.value
        accountService.createAccount(name)
        _toEditorPageFlow.emit(Unit)
    }

    override fun onCleared() {
        disposableBag.clear()
    }
}