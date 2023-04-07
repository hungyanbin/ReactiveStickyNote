package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.utils.mapOptional
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.rx3.asFlow

class TapCanvasUseCae(
    private val tapCanvasObservable: Observable<Unit>,
) {

    fun startFlow(editor: Editor): Flow<Any> {
        return tapCanvasObservable.asFlow()
            .map { editor.userSelectedNote.first() }
            .mapOptional { it }
            .onEach { selectedNote ->
                editor.setNoteUnSelected(selectedNote.noteId)
                editor.showAddButton()
            }
    }
}