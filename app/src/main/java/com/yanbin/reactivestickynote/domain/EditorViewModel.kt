package com.yanbin.reactivestickynote.domain

import androidx.lifecycle.ViewModel
import com.yanbin.reactivestickynote.model.Note
import com.yanbin.reactivestickynote.model.Position
import com.yanbin.reactivestickynote.model.YBColor
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

class EditorViewModel: ViewModel() {

    private val noteMap = ConcurrentHashMap<String, Note>()
    private val allNotesSubject = BehaviorSubject.create<List<Note>>()

    val allNotes: Observable<List<Note>> = allNotesSubject.hide()

    fun updateNotePosition(note: Note, positionDelta: Position) {
        val currentNote = noteMap[note.id]  ?: return
        val newNote = currentNote.copy(position = currentNote.position + positionDelta)
        setToNoteList(note.id, newNote)
    }

    private fun setToNoteList(noteId: String, newNote: Note) {
        noteMap[noteId] = newNote
        allNotesSubject.onNext(noteMap.elements().toList())
    }

    init {
        createRandomNote().let { note -> setToNoteList(note.id, note) }
        createRandomNote().let { note -> setToNoteList(note.id, note) }
    }

    private fun createRandomNote(): Note {
        val randomColorIndex = Random.nextInt(YBColor.defaultColors.size)
        val randomPosition = Position(Random.nextInt(-50, 50).toFloat(), Random.nextInt(-50, 50).toFloat())
        val randomId = UUID.randomUUID().toString()
        return Note(randomId, "Hello", randomPosition, YBColor.defaultColors[randomColorIndex])
    }
}