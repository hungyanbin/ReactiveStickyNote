package com.yanbin.reactivestickynote.editor.usecase

import io.reactivex.rxjava3.disposables.CompositeDisposable

open class BaseRxJavaUseCase {

    protected val disposableBag = CompositeDisposable()

    fun stop() {
        disposableBag.clear()
    }
}