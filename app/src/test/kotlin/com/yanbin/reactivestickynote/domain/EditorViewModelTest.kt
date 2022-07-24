package com.yanbin.reactivestickynote.domain

import com.yanbin.reactivestickynote.data.NoteRepository
import com.yanbin.reactivestickynote.model.Note
import com.yanbin.reactivestickynote.model.Position
import com.yanbin.reactivestickynote.model.YBColor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import org.junit.Test
import java.util.*

internal class EditorViewModelTest {

    private val noteRepository = mockk<NoteRepository>(relaxed = true)

    @Test
    fun loadStickyNoteTest() {
        every { noteRepository.getAllNotes() } returns Observable.just(fakeNotes())

        val viewModel = EditorViewModel(noteRepository)
        val testObserver = viewModel.allNotes.test()
        testObserver.assertValue(fakeNotes())
    }

    @Test
    fun `move note 1 with delta position (40, 40), expect noteRepository put Note with position (40, 40)`() {
        every { noteRepository.getAllNotes() } returns Observable.just(fakeNotes())

        val viewModel = EditorViewModel(noteRepository)

        viewModel.moveNote("1", Position(40f, 40f))

        verify { noteRepository.putNote(
            Note(id = "1", text = "text1", position = Position(40f, 40f), color = YBColor.Aquamarine)
        ) }
    }

    @Test
    fun `move note 2 with delta position (40, 40), expect noteRepository put Note with position (50, 50)`() {
        every { noteRepository.getAllNotes() } returns Observable.just(fakeNotes())

        val viewModel = EditorViewModel(noteRepository)

        viewModel.moveNote("2", Position(40f, 40f))

        verify { noteRepository.putNote(
            Note(id = "2", text = "text2", position = Position(50f, 50f), color = YBColor.Gorse)
        ) }
    }

    @Test
    fun `addNewNote called expect noteRepository add new note`() {
        every { noteRepository.getAllNotes() } returns Observable.just(emptyList())

        val viewModel = EditorViewModel(noteRepository)
        viewModel.addNewNote()

        verify { noteRepository.createNote(any()) }
    }

    @Test
    fun `tapNote called expect select the tapped note`() {
        every { noteRepository.getAllNotes() } returns Observable.just(fakeNotes())

        val viewModel = EditorViewModel(noteRepository)
        val selectingNoteObserver = viewModel.selectingNote.test()
        val tappedNote = fakeNotes()[0]
        viewModel.tapNote(tappedNote)

        selectingNoteObserver.assertValueAt(0, Optional.empty())
        selectingNoteObserver.assertValueAt(1, Optional.of(tappedNote))
    }

    @Test
    fun `tapCanvas called expect clear the selected note`() {
        every { noteRepository.getAllNotes() } returns Observable.just(fakeNotes())

        val viewModel = EditorViewModel(noteRepository)
        val selectingNoteObserver = viewModel.selectingNote.test()
        val tappedNote = fakeNotes()[0]
        viewModel.tapNote(tappedNote)
        viewModel.tapCanvas()

        selectingNoteObserver.assertValueAt(2, Optional.empty())
    }

    @Test
    fun `onDeleteClicked called expect clear the selected note`() {
        every { noteRepository.getAllNotes() } returns Observable.just(fakeNotes())

        val viewModel = EditorViewModel(noteRepository)
        val selectingNoteObserver = viewModel.selectingNote.test()
        val tappedNote = fakeNotes()[0]
        viewModel.tapNote(tappedNote)
        viewModel.onDeleteClicked()

        selectingNoteObserver.assertValueAt(2, Optional.empty())
    }

    @Test
    fun `onDeleteClicked called expect delete the note in noteRepository`() {
        every { noteRepository.getAllNotes() } returns Observable.just(fakeNotes())

        val viewModel = EditorViewModel(noteRepository)
        val tappedNote = fakeNotes()[0]
        viewModel.tapNote(tappedNote)
        viewModel.onDeleteClicked()

        verify { noteRepository.deleteNote(tappedNote.id) }
    }

    @Test
    fun `onEditTextClicked called expect openEditTextScreen`() {
        every { noteRepository.getAllNotes() } returns Observable.just(fakeNotes())

        val viewModel = EditorViewModel(noteRepository)
        val openEditTextScreenObserver = viewModel.openEditTextScreen.test()
        val tappedNote = fakeNotes()[0]
        viewModel.tapNote(tappedNote)
        viewModel.onEditTextClicked()

        openEditTextScreenObserver.assertValue(tappedNote)
    }

    @Test
    fun `tapNote called expect showing correct selectingColor`() {
        every { noteRepository.getAllNotes() } returns Observable.just(fakeNotes())

        val viewModel = EditorViewModel(noteRepository)
        val selectingColorObserver = viewModel.selectingColor.test()
        val tappedNote = fakeNotes()[0]
        viewModel.tapNote(tappedNote)

        selectingColorObserver.assertValue(tappedNote.color)
    }

    @Test
    fun `onColorSelected called expect update note with selected color`() {
        every { noteRepository.getAllNotes() } returns Observable.just(fakeNotes())
        val selectedColor = YBColor.PaleCanary

        val viewModel = EditorViewModel(noteRepository)
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

