package com.yanbin.reactivestickynote.editor.domain

import com.yanbin.reactivestickynote.stickynote.model.StickyNote
import com.yanbin.reactivestickynote.stickynote.model.YBColor
import com.yanbin.utils.mapOptional
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.map
import java.util.*

class ContextMenu(
    private val selectedNote: Flow<Optional<StickyNote>>
) {

    private val _contextMenuEvents = MutableSharedFlow<ContextMenuEvent>()

    val colorOptions: List<YBColor> = YBColor.defaultColors

    val selectedColor: Flow<YBColor> = selectedNote
        .mapOptional { it }
        .map { it.color }

    val contextMenuEvents: SharedFlow<ContextMenuEvent> = _contextMenuEvents

    suspend fun onColorSelected(color: YBColor) {
        _contextMenuEvents.emit(ContextMenuEvent.ChangeColor(color))
    }

    suspend fun onDeleteClicked() {
        _contextMenuEvents.emit(ContextMenuEvent.DeleteNote)
    }

    suspend fun onEditTextClicked() {
        _contextMenuEvents.emit(ContextMenuEvent.NavigateToEditTextPage)
    }

}

sealed interface ContextMenuEvent {
    object NavigateToEditTextPage: ContextMenuEvent
    object DeleteNote: ContextMenuEvent
    class ChangeColor(val color: YBColor): ContextMenuEvent
}
