package com.yanbin.reactivestickynote.login

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.account.AccountService
import com.yanbin.utils.fromIO
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

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
        accountService.createAccount(name)
            .fromIO()
            .subscribe { _ ->
                _toEditorPage.onNext(Unit)
            }
            .addTo(disposableBag)
    }

    override fun onCleared() {
        disposableBag.clear()
    }
}