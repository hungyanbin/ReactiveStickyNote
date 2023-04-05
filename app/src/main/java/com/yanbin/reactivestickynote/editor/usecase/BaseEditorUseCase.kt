package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseEditorUseCase {

    protected val disposableBag = CompositeDisposable()

    abstract fun start(editor: Editor, noteRepository: NoteRepository)

    fun stop() {
        disposableBag.clear()
    }
}