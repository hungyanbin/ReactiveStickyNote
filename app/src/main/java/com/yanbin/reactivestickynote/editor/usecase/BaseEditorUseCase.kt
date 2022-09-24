package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.Editor
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseEditorUseCase {

    protected val disposableBag = CompositeDisposable()

    abstract fun start(editor: Editor)

    fun stop() {
        disposableBag.clear()
    }
}