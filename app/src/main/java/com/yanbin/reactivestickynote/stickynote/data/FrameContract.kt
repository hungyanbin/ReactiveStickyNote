package com.yanbin.reactivestickynote.stickynote.data

import kotlinx.serialization.Serializable

@Serializable
data class FrameContract(
    val type: Type,
    val notes: List<NoteDto>,
    val updatedNoteAttributes: UpdatedNoteAttributes?,
) {
    enum class Type {
        AllNotes, UpdateNote, Create, Delete
    }

    fun isValid() = when (type) {
        Type.AllNotes -> notes.isNotEmpty()
        Type.UpdateNote -> updatedNoteAttributes != null
        Type.Create -> updatedNoteAttributes != null
        Type.Delete -> updatedNoteAttributes != null
    }
}
