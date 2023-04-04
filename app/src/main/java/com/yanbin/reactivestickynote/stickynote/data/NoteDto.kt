package com.yanbin.reactivestickynote.stickynote.data

import com.yanbin.common.YBSize
import com.yanbin.reactivestickynote.stickynote.model.Position
import com.yanbin.reactivestickynote.stickynote.model.StickyNote
import com.yanbin.reactivestickynote.stickynote.model.YBColor
import kotlinx.serialization.Serializable

@Serializable
data class NoteDto(
    val id: String,
    val text: String,
    val color: Long,
    val posX: Float,
    val posY: Float,
    val width: Float,
    val height: Float,
)

fun NoteDto.toStickyNote() = StickyNote(
    id = id,
    text = text,
    position = Position(posX, posY),
    size = YBSize(width, height),
    color = YBColor(color)
)