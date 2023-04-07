package com.yanbin.reactivestickynote.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanbin.reactivestickynote.account.AccountService
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.launch

class LoginViewModel(
    private val accountService: AccountService
): ViewModel() {

    private val disposableBag = CompositeDisposable()

    private val _text = BehaviorSubject.createDefault("")
    val text: Observable<String> = _text.hide()

    private val _toEditorPage = PublishSubject.create<Unit>()
    val toEditorPage: Observable<Unit> = _toEditorPage.hide()

    fun onTextChanged(text: String) {
        _text.onNext(text)
    }

    fun onEnterClicked() {
        val name = _text.value ?: return
        viewModelScope.launch {
            accountService.createAccount(name)
            _toEditorPage.onNext(Unit)
        }
    }

    override fun onCleared() {
        disposableBag.clear()
    }
}