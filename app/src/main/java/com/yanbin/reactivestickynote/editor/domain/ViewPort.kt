package com.yanbin.reactivestickynote.editor.domain

import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.reactivestickynote.stickynote.model.Position
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.rx3.asObservable

class ViewPort(noteRepository: NoteRepository) {

    private val _center = BehaviorSubject.createDefault(Position(0f, 0f))
    private val _scale = BehaviorSubject.createDefault(1f)

    val allVisibleNoteIds: Observable<List<String>> = noteRepository.getAllVisibleNoteIds().asObservable()
    val scale: Observable<Float> = _scale.hide()
    val center: Observable<Position> = _center.hide()

    fun transformDelta(position: Position, scale: Float) {
        _center.onNext(position + _center.value!!)
        _scale.onNext((scale * _scale.value!!).coerceIn(MIN_SCALE, MAX_SCALE))
    }

    companion object {
        const val MIN_SCALE = 0.3f
        const val MAX_SCALE = 3f
    }
}