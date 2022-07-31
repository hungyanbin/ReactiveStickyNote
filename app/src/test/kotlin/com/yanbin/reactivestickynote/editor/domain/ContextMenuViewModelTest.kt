package com.yanbin.reactivestickynote.editor.domain

import com.yanbin.reactivestickynote.editor.data.NoteRepository
import com.yanbin.reactivestickynote.editor.model.Position
import com.yanbin.reactivestickynote.editor.model.StickyNote
import com.yanbin.reactivestickynote.editor.model.YBColor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Test
import java.util.*

class ContextMenuViewModelTest {

    private val noteRepository = mockk<NoteRepository>(relaxed = true)
    private val stickyNoteEditor = StickyNoteEditor(noteRepository)

    @Before
    fun setUp() {
        stickyNoteEditor.start()
    }

    @Test
    fun `onDeleteClicked called expect clear the selected note`() {
        every { noteRepository.getNoteById("1") } returns Observable.just(fakeNotes()[0])

        val selectingNoteObserver = stickyNoteEditor.selectedNote.test()
        stickyNoteEditor.selectNote("1")
        stickyNoteEditor.contextMenu.onDeleteClicked()

        selectingNoteObserver.assertValueAt(2, Optional.empty())
    }

    @Test
    fun `onDeleteClicked called expect delete the note in noteRepository`() {
        every { noteRepository.getNoteById("1") } returns Observable.just(fakeNotes()[0])

        stickyNoteEditor.selectNote("1")
        stickyNoteEditor.contextMenu.onDeleteClicked()

        verify { noteRepository.deleteNote("1") }
    }

    @Test
    fun `onEditTextClicked called expect openEditTextScreen`() {
        val tappedNote = StickyNote(id = "1", text = "text1", position = Position(0f, 0f), color = YBColor.Aquamarine)
        every { noteRepository.getNoteById("1") } returns Observable.just(tappedNote)

        val openEditTextScreenObserver = stickyNoteEditor.openEditTextScreen.test()
        stickyNoteEditor.selectNote("1")
        stickyNoteEditor.contextMenu.onEditTextClicked()

        openEditTextScreenObserver.assertValue(tappedNote)
    }

    @Test
    fun `tapNote called expect showing correct selectingColor`() {
        val tappedNote = StickyNote(id = "1", text = "text1", position = Position(0f, 0f), color = YBColor.Aquamarine)
        every { noteRepository.getNoteById("1") } returns Observable.just(tappedNote)

        val selectingColorObserver = stickyNoteEditor.contextMenu.selectedColor.test()
        stickyNoteEditor.selectNote("1")

        selectingColorObserver.assertValue(YBColor.Aquamarine)
    }

    @Test
    fun `onColorSelected called expect update note with selected color`() {
        val tappedNote = StickyNote(id = "1", text = "text1", position = Position(0f, 0f), color = YBColor.Aquamarine)
        every { noteRepository.getNoteById("1") } returns Observable.just(tappedNote)
        val selectedColor = YBColor.PaleCanary

        stickyNoteEditor.selectNote("1")
        stickyNoteEditor.contextMenu.onColorSelected(selectedColor)

        verify { noteRepository.putNote(
            StickyNote(id = "1", text = "text1", position = Position(0f, 0f), color = YBColor.PaleCanary)
        ) }
    }

    private fun fakeNotes(): List<StickyNote> {
        return listOf(
            StickyNote(id = "1", text = "text1", position = Position(0f, 0f), color = YBColor.Aquamarine),
            StickyNote(id = "2", text = "text2", position = Position(10f, 10f), color = YBColor.Gorse),
            StickyNote(id = "3", text = "text3", position = Position(20f, 20f), color = YBColor.HotPink),
        )
    }
}