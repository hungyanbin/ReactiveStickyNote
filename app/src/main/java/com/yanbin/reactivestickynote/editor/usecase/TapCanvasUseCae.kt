package com.yanbin.reactivestickynote.editor.usecase

import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.utils.mapOptional
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.rx3.asFlow

class TapCanvasUseCae(
    private val tapCanvasObservable: Observable<Unit>,
    private val scope: CoroutineScope
): BaseEditorUseCase() {

    override fun start(editor: Editor, noteRepository: NoteRepository) {
        tapCanvasObservable.asFlow()
            .map { editor.userSelectedNote.first() }
            .mapOptional { it }
            .onEach { selectedNote ->
                editor.setNoteUnSelected(selectedNote.noteId)
                editor.showAddButton()
            }
            .launchIn(scope)
    }
}