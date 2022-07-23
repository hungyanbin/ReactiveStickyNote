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

class EditTextViewModelTest {

    private val noteRepository = mockk<NoteRepository>(relaxed = true)
    private val noteId = "1"
    private val defaultText = "text1"

    @Test
    fun `load correct text from start`() {
        every { noteRepository.getAllNotes() } returns Observable.just(fakeNotes())

        val viewModel = EditTextViewModel(noteRepository, noteId, defaultText)
        val textObserver = viewModel.text.test()

        textObserver.assertValue("text1")
    }

    @Test
    fun `change text expect show the correct text`() {
        every { noteRepository.getAllNotes() } returns Observable.just(fakeNotes())

        val viewModel = EditTextViewModel(noteRepository, noteId, defaultText)
        val textObserver = viewModel.text.test()
        viewModel.onTextChanged("text1 changed")

        textObserver.assertValueAt(1, "text1 changed")
    }

    @Test
    fun `onConfirmClicked expect update note with new text`() {
        val note = fakeNotes().find { it.id == noteId }!!
        every { noteRepository.getAllNotes() } returns Observable.just(fakeNotes())
        every { noteRepository.getNoteById(noteId) } returns Observable.just(note)

        val viewModel = EditTextViewModel(noteRepository, noteId, defaultText)
        viewModel.onTextChanged("text1 changed")
        viewModel.onConfirmClicked()

        verify { noteRepository.putNote(note.copy(text = "text1 changed")) }
    }

    @Test
    fun `onConfirmClicked expect leave page`() {
        val note = fakeNotes().find { it.id == noteId }!!
        every { noteRepository.getAllNotes() } returns Observable.just(fakeNotes())
        every { noteRepository.getNoteById(noteId) } returns Observable.just(note)

        val viewModel = EditTextViewModel(noteRepository, noteId, defaultText)
        val leavePageObserver = viewModel.leavePage.test()
        viewModel.onTextChanged("text1 changed")
        viewModel.onConfirmClicked()

        leavePageObserver.assertValue(Unit)
    }

    @Test
    fun `onCancelClicked expect leave page`() {
        every { noteRepository.getAllNotes() } returns Observable.just(fakeNotes())

        val viewModel = EditTextViewModel(noteRepository, noteId, defaultText)
        val leavePageObserver = viewModel.leavePage.test()
        viewModel.onCancelClicked()

        leavePageObserver.assertValue(Unit)
    }

    private fun fakeNotes(): List<Note> {
        return listOf(
            Note(id = "1", text = "text1", position = Position(0f, 0f), color = YBColor.Aquamarine),
            Note(id = "2", text = "text2", position = Position(10f, 10f), color = YBColor.Gorse),
            Note(id = "3", text = "text3", position = Position(20f, 20f), color = YBColor.HotPink),
        )
    }
}