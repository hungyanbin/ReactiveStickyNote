package com.yanbin.reactivestickynote.di

import com.yanbin.reactivestickynote.data.FirebaseNoteRepository
import com.yanbin.reactivestickynote.data.NoteRepository
import com.yanbin.reactivestickynote.ui.vm.ContextMenuViewModel
import com.yanbin.reactivestickynote.domain.EditTextViewModel
import com.yanbin.reactivestickynote.ui.vm.EditorViewModel
import com.yanbin.reactivestickynote.domain.StickyNoteEditor
import com.yanbin.reactivestickynote.ui.vm.StickyNoteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun getNoteModule() =
    module {
        viewModel {
            EditorViewModel(
                stickyNoteEditor = get()
            )
        }

        viewModel {
            ContextMenuViewModel(
                contextMenu = get<StickyNoteEditor>().contextMenu
            )
        }

        viewModel { (noteId: String, defaultText: String) ->
            EditTextViewModel(
                noteRepository = get(),
                noteId = noteId,
                defaultText = defaultText
            )
        }

        single { StickyNoteEditor(
            noteRepository = get()
        ) }

        viewModel {
            StickyNoteViewModel(
                stickyNoteEditor = get()
            )
        }

        single<NoteRepository> {
            FirebaseNoteRepository()
        }
    }
