package com.yanbin.reactivestickynote.stickynote.data

import com.yanbin.common.YBPointF
import com.yanbin.common.YBSize
import com.yanbin.reactivestickynote.stickynote.model.NoteAttribute
import com.yanbin.reactivestickynote.stickynote.model.Position
import com.yanbin.reactivestickynote.stickynote.model.StickyNote
import com.yanbin.reactivestickynote.stickynote.model.YBColor
import kotlinx.serialization.Serializable

@Serializable
class SocketMessage(
    val type: Type,
    val updatedAttributes: UpdatedNoteAttributes?,
) {
    enum class Type {
        Query, Update, Create, Delete
    }

    companion object {
        fun query(): SocketMessage {
            return SocketMessage(Type.Query, null)
        }

        fun update(updatedAttributes: UpdatedNoteAttributes): SocketMessage {
            return SocketMessage(Type.Update, updatedAttributes)
        }

        fun create(updatedAttributes: UpdatedNoteAttributes): SocketMessage {
            return SocketMessage(Type.Create, updatedAttributes)
        }

        fun delete(id: String): SocketMessage {
            return SocketMessage(Type.Delete, UpdatedNoteAttributes.fromAttributes(id, emptyList()))
        }
    }
}

@Serializable
data class UpdatedNoteAttributes(
    val objectId: String,
    val position: YBPointF?,
    val size: YBSize?,
    val color: Long?,
    val text: String?
) {

    fun updateNote(stickyNote: StickyNote): StickyNote {
        val updatedPosition = position ?: YBPointF(stickyNote.position.x, stickyNote.position.y)
        val updatedSize = size ?: stickyNote.size
        val updatedColor = color ?: stickyNote.color.color
        val updatedText = text ?: stickyNote.text
        return StickyNote(
            stickyNote.id,
            updatedText,
            Position(updatedPosition.x, updatedPosition.y),
            updatedSize,
            YBColor(updatedColor)
        )
    }

    fun toStickyNote(): StickyNote {
        requireNotNull(position)
        requireNotNull(size)
        requireNotNull(color)
        requireNotNull(text)
        val newPosition = Position(position.x, position.y)
        return StickyNote(objectId, text, newPosition, size, YBColor(color))
    }
    companion object {
        fun fromAttributes(noteId: String, attributes: List<NoteAttribute>): UpdatedNoteAttributes {
            var position: YBPointF? = null
            var size: YBSize? = null
            var color: Long? = null
            var text: String? = null
            attributes.forEach {
                when (it) {
                    is NoteAttribute.Pos -> position = YBPointF(it.position.x, it.position.y)
                    is NoteAttribute.Size -> size = it.size
                    is NoteAttribute.Color -> color = it.color.color
                    is NoteAttribute.Text -> text = it.text
                }
            }
            return UpdatedNoteAttributes(noteId, position, size, color, text)
        }

        fun fromStickyNote(stickyNote: StickyNote): UpdatedNoteAttributes {
            return UpdatedNoteAttributes(
                stickyNote.id,
                YBPointF(stickyNote.position.x, stickyNote.position.y),
                stickyNote.size,
                stickyNote.color.color,
                stickyNote.text
            )
        }
    }
}