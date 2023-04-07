package com.yanbin.reactivestickynote.editor.domain

import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.reactivestickynote.stickynote.model.Position
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.rx3.asObservable

class ViewPort(noteRepository: NoteRepository) {

    private val _center = MutableStateFlow(Position(0f, 0f))
    private val _scale = MutableStateFlow(1f)

    val allVisibleNoteIds: Flow<List<String>> = noteRepository.getAllVisibleNoteIds()
    val scale: StateFlow<Float> = _scale
    val center: StateFlow<Position> = _center

    suspend fun transformDelta(position: Position, scale: Float) {
        _center.emit(position + _center.value)
        _scale.emit((scale * _scale.value).coerceIn(MIN_SCALE, MAX_SCALE))
    }

    companion object {
        const val MIN_SCALE = 0.3f
        const val MAX_SCALE = 3f
    }
}