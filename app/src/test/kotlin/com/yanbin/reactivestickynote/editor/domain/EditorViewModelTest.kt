package com.yanbin.reactivestickynote.editor.domain

import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.reactivestickynote.stickynote.model.Position
import com.yanbin.reactivestickynote.stickynote.model.StickyNote
import com.yanbin.reactivestickynote.stickynote.model.YBColor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import org.junit.Test
import java.util.*

internal class EditorViewModelTest {

    private val noteRepository = mockk<NoteRepository>(relaxed = true)
    private val stickyNoteEditor = StickyNoteEditor(noteRepository)

    @Test
    fun loadStickyNoteTest() {
        every { noteRepository.getAllVisibleNoteIds() } returns Observable.just(fakeNoteIds())

        val testObserver = stickyNoteEditor.allVisibleNoteIds.test()
        testObserver.assertValue(fakeNoteIds())
    }

    @Test
    fun `move note 1 with delta position (40, 40), expect noteRepository put Note with position (40, 40)`() {
        every { noteRepository.getNoteById("1") } returns Observable.just(fakeNotes()[0])

        stickyNoteEditor.moveNote("1", Position(40f, 40f))

        verify { noteRepository.putNote(
            StickyNote(id = "1", text = "text1", position = Position(40f, 40f), color = YBColor.Aquamarine)
        ) }
    }

    @Test
    fun `move note 2 with delta position (40, 40), expect noteRepository put Note with position (50, 50)`() {
        every { noteRepository.getNoteById("2") } returns Observable.just(fakeNotes()[1])

        stickyNoteEditor.moveNote("2", Position(40f, 40f))

        verify { noteRepository.putNote(
            StickyNote(id = "2", text = "text2", position = Position(50f, 50f), color = YBColor.Gorse)
        ) }
    }

    @Test
    fun `addNewNote called expect noteRepository add new note`() {
        stickyNoteEditor.addNewNote()

        verify { noteRepository.createNote(any()) }
    }

    @Test
    fun `tapNote called expect select the tapped note`() {
        every { noteRepository.getNoteById("1") } returns Observable.just(fakeNotes()[0])

        val selectingNoteObserver = stickyNoteEditor.selectedStickyNote.test()
        val tappedNote = fakeNotes()[0]
        stickyNoteEditor.selectNote("1")

        selectingNoteObserver.assertValueAt(0, Optional.empty())
        selectingNoteObserver.assertValueAt(1, Optional.of(tappedNote))
    }

    @Test
    fun `clearSelection called expect clear the selected note`() {
        every { noteRepository.getNoteById("1") } returns Observable.just(fakeNotes()[0])

        val selectingNoteObserver = stickyNoteEditor.selectedStickyNote.test()
        stickyNoteEditor.selectNote("1")
        stickyNoteEditor.clearSelection()

        selectingNoteObserver.assertValueAt(2, Optional.empty())
    }

    private fun fakeNoteIds() = fakeNotes().map { it.id }

    private fun fakeNotes(): List<StickyNote> {
        return listOf(
            StickyNote(id = "1", text = "text1", position = Position(0f, 0f), color = YBColor.Aquamarine),
            StickyNote(id = "2", text = "text2", position = Position(10f, 10f), color = YBColor.Gorse),
            StickyNote(id = "3", text = "text3", position = Position(20f, 20f), color = YBColor.HotPink),
        )
    }
}

