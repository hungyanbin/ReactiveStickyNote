package com.yanbin.reactivestickynote.stickynote.data

import kotlinx.serialization.Serializable

@Serializable
data class FrameContract(
    val type: Type,
    val notes: List<NoteDto>,
    val updatedAttributes: UpdatedAttributes?
) {
    enum class Type {
        AllNotes, UpdateNote
    }

    fun isValid() = when (type) {
        Type.AllNotes -> notes.isNotEmpty()
        Type.UpdateNote -> updatedAttributes != null
    }
}
