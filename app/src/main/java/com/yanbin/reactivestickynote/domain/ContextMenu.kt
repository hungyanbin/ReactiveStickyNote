package com.yanbin.reactivestickynote.domain

import com.yanbin.reactivestickynote.model.StickyNote
import com.yanbin.reactivestickynote.model.YBColor
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*

class ContextMenu(
    private val selectedNote: Observable<Optional<StickyNote>>
) {

    private val _contextMenuEvents = PublishSubject.create<ContextMenuEvent>()

    val colorOptions: List<YBColor> = YBColor.defaultColors

    val selectedColor: Observable<YBColor> = selectedNote
        .mapOptional { it }
        .map { it.color }

    val contextMenuEvents: Observable<ContextMenuEvent> = _contextMenuEvents.hide()

    fun onColorSelected(color: YBColor) {
        _contextMenuEvents.onNext(ContextMenuEvent.ChangeColor(color))
    }

    fun onDeleteClicked() {
        _contextMenuEvents.onNext(ContextMenuEvent.DeleteNote)
    }

    fun onEditTextClicked() {
        _contextMenuEvents.onNext(ContextMenuEvent.NavigateToEditTextPage)
    }

}

sealed interface ContextMenuEvent {
    object NavigateToEditTextPage: ContextMenuEvent
    object DeleteNote: ContextMenuEvent
    class ChangeColor(val color: YBColor): ContextMenuEvent
}
