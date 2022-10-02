package com.yanbin.reactivestickynote.di

import com.yanbin.reactivestickynote.data.FirebaseNoteRepository
import com.yanbin.reactivestickynote.data.NoteRepository
import com.yanbin.reactivestickynote.ui.vm.ContextMenuViewModel
import com.yanbin.reactivestickynote.domain.EditTextViewModel
import com.yanbin.reactivestickynote.ui.vm.EditorViewModel
import com.yanbin.reactivestickynote.domain.Editor
import com.yanbin.reactivestickynote.ui.vm.StickyNoteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun getNoteModule() =
    module {
        viewModel {
            EditorViewModel(
                editor = get()
            )
        }

        viewModel {
            ContextMenuViewModel(
                contextMenu = get<Editor>().contextMenu
            )
        }

        viewModel { (noteId: String, defaultText: String) ->
            EditTextViewModel(
                noteRepository = get(),
                noteId = noteId,
                defaultText = defaultText
            )
        }

        single { Editor(
            noteRepository = get()
        ) }

        viewModel {
            StickyNoteViewModel(
                editor = get()
            )
        }

        single<NoteRepository> {
            FirebaseNoteRepository()
        }
    }
