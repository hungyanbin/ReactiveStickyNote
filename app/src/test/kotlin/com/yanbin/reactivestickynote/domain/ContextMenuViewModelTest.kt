package com.yanbin.reactivestickynote.domain

import com.yanbin.reactivestickynote.model.Note
import com.yanbin.reactivestickynote.model.Position
import com.yanbin.reactivestickynote.model.YBColor
import io.mockk.every
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import org.junit.Test
import java.util.*

// Implement With NoteEditor
class ContextMenuViewModelTest {

//    @Test
//    fun `onDeleteClicked called expect clear the selected note`() {
//        val viewModel = ContextMenuViewModel(contextMenu)
//        val selectingNoteObserver = viewModel.selectingNote.test()
//        viewModel.tapNote(tappedNote)
//        viewModel.onDeleteClicked()
//
//        selectingNoteObserver.assertValueAt(2, Optional.empty())
//    }

//    @Test
//    fun `onDeleteClicked called expect delete the note in noteRepository`() {
//        val viewModel = EditorViewModel(noteEditor)
//        val tappedNote = fakeNotes()[0]
//        viewModel.tapNote(tappedNote)
//        viewModel.onDeleteClicked()
//
//        verify { noteRepository.deleteNote(tappedNote.id) }
//    }
//
//    @Test
//    fun `onEditTextClicked called expect openEditTextScreen`() {
//        every { noteRepository.getNoteById("1") } returns Observable.just(fakeNotes()[0])
//
//        val viewModel = EditorViewModel(noteEditor)
//        val openEditTextScreenObserver = viewModel.openEditTextScreen.test()
//        val tappedNote = fakeNotes()[0]
//        viewModel.tapNote(tappedNote)
//        viewModel.onEditTextClicked()
//
//        openEditTextScreenObserver.assertValue(tappedNote)
//    }
//
//    @Test
//    fun `tapNote called expect showing correct selectingColor`() {
//        every { noteRepository.getNoteById("1") } returns Observable.just(fakeNotes()[0])
//
//        val viewModel = EditorViewModel(noteEditor)
//        val selectingColorObserver = viewModel.selectingColor.test()
//        val tappedNote = fakeNotes()[0]
//        viewModel.tapNote(tappedNote)
//
//        selectingColorObserver.assertValue(tappedNote.color)
//    }
//
//    @Test
//    fun `onColorSelected called expect update note with selected color`() {
//        every { noteRepository.getNoteById("1") } returns Observable.just(fakeNotes()[0])
//        val selectedColor = YBColor.PaleCanary
//
//        val viewModel = EditorViewModel(noteEditor)
//        val tappedNote = fakeNotes()[0]
//        viewModel.tapNote(tappedNote)
//        viewModel.onColorSelected(selectedColor)
//
//        verify { noteRepository.putNote(
//            Note(id = "1", text = "text1", position = Position(0f, 0f), color = YBColor.PaleCanary)
//        ) }
//    }
}