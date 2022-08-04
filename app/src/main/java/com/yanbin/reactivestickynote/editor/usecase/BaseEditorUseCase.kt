package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.StickyNoteEditor
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseEditorUseCase {

    protected val disposableBag = CompositeDisposable()

    abstract fun start(stickyNoteEditor: StickyNoteEditor)

    fun stop() {
        disposableBag.clear()
    }
}