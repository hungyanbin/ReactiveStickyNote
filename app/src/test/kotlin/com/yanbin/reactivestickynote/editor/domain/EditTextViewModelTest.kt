package com.yanbin.reactivestickynote.editor.domain

import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.reactivestickynote.edittext.EditTextViewModel
import com.yanbin.reactivestickynote.stickynote.model.Position
import com.yanbin.reactivestickynote.stickynote.model.StickyNote
import com.yanbin.reactivestickynote.stickynote.model.YBColor
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
        val viewModel = EditTextViewModel(noteRepository, noteId, defaultText)
        val textObserver = viewModel.text.test()

        textObserver.assertValue("text1")
    }

    @Test
    fun `change text expect show the correct text`() {
        val viewModel = EditTextViewModel(noteRepository, noteId, defaultText)
        val textObserver = viewModel.text.test()
        viewModel.onTextChanged("text1 changed")

        textObserver.assertValueAt(1, "text1 changed")
    }

    @Test
    fun `onConfirmClicked expect update note with new text`() {
        val note = StickyNote(id = "1", text = "text1", position = Position(0f, 0f), color = YBColor.Aquamarine)
        every { noteRepository.getNoteById(noteId) } returns Observable.just(note)

        val viewModel = EditTextViewModel(noteRepository, noteId, defaultText)
        viewModel.onTextChanged("text1 changed")
        viewModel.onConfirmClicked()

        verify { noteRepository.putNote(
            StickyNote(id = "1", text = "text1 changed", position = Position(0f, 0f), color = YBColor.Aquamarine)
        ) }
    }

    @Test
    fun `onConfirmClicked expect leave page`() {
        val note = StickyNote(id = "1", text = "text1", position = Position(0f, 0f), color = YBColor.Aquamarine)
        every { noteRepository.getNoteById(noteId) } returns Observable.just(note)

        val viewModel = EditTextViewModel(noteRepository, noteId, defaultText)
        val leavePageObserver = viewModel.leavePage.test()
        viewModel.onTextChanged("text1 changed")
        viewModel.onConfirmClicked()

        leavePageObserver.assertValue(Unit)
    }

    @Test
    fun `onCancelClicked expect leave page`() {
        val viewModel = EditTextViewModel(noteRepository, noteId, defaultText)
        val leavePageObserver = viewModel.leavePage.test()
        viewModel.onCancelClicked()

        leavePageObserver.assertValue(Unit)
    }
}