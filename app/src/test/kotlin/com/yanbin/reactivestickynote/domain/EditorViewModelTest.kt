package com.yanbin.reactivestickynote.domain

import com.yanbin.reactivestickynote.data.NoteRepository
import com.yanbin.reactivestickynote.model.Note
import com.yanbin.reactivestickynote.model.Position
import com.yanbin.reactivestickynote.model.YBColor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*

internal class EditorViewModelTest {

    private val noteRepository = mockk<NoteRepository>(relaxed = true)
    private val noteEditor = NoteEditor(noteRepository)

    @Test
    fun loadStickyNoteTest() {
        every { noteRepository.getAllVisibleNoteIds() } returns Observable.just(fakeNotes().map { it.id })

        val viewModel = EditorViewModel(noteEditor)
        val testObserver = viewModel.allVisibleNoteIds.test()
        testObserver.assertValue(fakeNotes().map { it.id })
    }

    @Test
    fun `move note 1 with delta position (40, 40), expect noteRepository put Note with position (40, 40)`() {
        every { noteRepository.getNoteById("1") } returns Observable.just(fakeNotes()[0])

        val viewModel = EditorViewModel(noteEditor)

        viewModel.moveNote("1", Position(40f, 40f))

        verify { noteRepository.putNote(
            Note(id = "1", text = "text1", position = Position(40f, 40f), color = YBColor.Aquamarine)
        ) }
    }

    @Test
    fun `move note 2 with delta position (40, 40), expect noteRepository put Note with position (50, 50)`() {
        every { noteRepository.getNoteById("2") } returns Observable.just(fakeNotes()[1])

        val viewModel = EditorViewModel(noteEditor)

        viewModel.moveNote("2", Position(40f, 40f))

        verify { noteRepository.putNote(
            Note(id = "2", text = "text2", position = Position(50f, 50f), color = YBColor.Gorse)
        ) }
    }

    @Test
    fun `addNewNote called expect noteRepository add new note`() {
        val viewModel = EditorViewModel(noteEditor)
        viewModel.addNewNote()

        verify { noteRepository.createNote(any()) }
    }

    @Test
    fun `tapNote called expect select the tapped note`() {
        every { noteRepository.getNoteById("1") } returns Observable.just(fakeNotes()[0])

        val viewModel = EditorViewModel(noteEditor)
        val selectingNoteObserver = viewModel.selectingNote.test()
        val tappedNote = fakeNotes()[0]
        viewModel.tapNote(tappedNote)

        selectingNoteObserver.assertValueAt(0, Optional.empty())
        selectingNoteObserver.assertValueAt(1, Optional.of(tappedNote))
    }

    @Test
    fun `tapCanvas called expect clear the selected note`() {
        every { noteRepository.getNoteById("1") } returns Observable.just(fakeNotes()[0])

        val viewModel = EditorViewModel(noteEditor)
        val selectingNoteObserver = viewModel.selectingNote.test()
        val tappedNote = fakeNotes()[0]
        viewModel.tapNote(tappedNote)
        viewModel.tapCanvas()

        selectingNoteObserver.assertValueAt(2, Optional.empty())
    }

    @Test
    fun `onDeleteClicked called expect clear the selected note`() {
        every { noteRepository.getNoteById("1") } returns Observable.just(fakeNotes()[0])

        val viewModel = EditorViewModel(noteEditor)
        val selectingNoteObserver = viewModel.selectingNote.test()
        val tappedNote = fakeNotes()[0]
        viewModel.tapNote(tappedNote)
        viewModel.onDeleteClicked()

        selectingNoteObserver.assertValueAt(2, Optional.empty())
    }

    @Test
    fun `onDeleteClicked called expect delete the note in noteRepository`() {
        val viewModel = EditorViewModel(noteEditor)
        val tappedNote = fakeNotes()[0]
        viewModel.tapNote(tappedNote)
        viewModel.onDeleteClicked()

        verify { noteRepository.deleteNote(tappedNote.id) }
    }

    @Test
    fun `onEditTextClicked called expect openEditTextScreen`() {
        every { noteRepository.getNoteById("1") } returns Observable.just(fakeNotes()[0])

        val viewModel = EditorViewModel(noteEditor)
        val openEditTextScreenObserver = viewModel.openEditTextScreen.test()
        val tappedNote = fakeNotes()[0]
        viewModel.tapNote(tappedNote)
        viewModel.onEditTextClicked()

        openEditTextScreenObserver.assertValue(tappedNote)
    }

    @Test
    fun `tapNote called expect showing correct selectingColor`() {
        every { noteRepository.getNoteById("1") } returns Observable.just(fakeNotes()[0])

        val viewModel = EditorViewModel(noteEditor)
        val selectingColorObserver = viewModel.selectingColor.test()
        val tappedNote = fakeNotes()[0]
        viewModel.tapNote(tappedNote)

        selectingColorObserver.assertValue(tappedNote.color)
    }

    @Test
    fun `onColorSelected called expect update note with selected color`() {
        every { noteRepository.getNoteById("1") } returns Observable.just(fakeNotes()[0])
        val selectedColor = YBColor.PaleCanary

        val viewModel = EditorViewModel(noteEditor)
        val tappedNote = fakeNotes()[0]
        viewModel.tapNote(tappedNote)
        viewModel.onColorSelected(selectedColor)

        verify { noteRepository.putNote(
            Note(id = "1", text = "text1", position = Position(0f, 0f), color = YBColor.PaleCanary)
        ) }
    }

    private fun fakeNotes(): List<Note> {
        return listOf(
            Note(id = "1", text = "text1", position = Position(0f, 0f), color = YBColor.Aquamarine),
            Note(id = "2", text = "text2", position = Position(10f, 10f), color = YBColor.Gorse),
            Note(id = "3", text = "text3", position = Position(20f, 20f), color = YBColor.HotPink),
        )
    }
}

