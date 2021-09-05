package com.yanbin.reactivestickynote.di

import com.yanbin.reactivestickynote.data.FirebaseFacade
import com.yanbin.reactivestickynote.data.FirebaseNoteRepository
import com.yanbin.reactivestickynote.data.NoteRepository
import com.yanbin.reactivestickynote.domain.ContextMenuViewModel
import com.yanbin.reactivestickynote.domain.EditTextViewModel
import com.yanbin.reactivestickynote.domain.EditorViewModel
import com.yanbin.reactivestickynote.domain.NoteEditor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun getNoteModule() =
    module {
        viewModel {
            EditorViewModel(
                noteEditor = get()
            )
        }

        viewModel {
            ContextMenuViewModel(
                contextMenu = get<NoteEditor>().contextMenu
            )
        }

        viewModel { (noteId: String) ->
            EditTextViewModel(
                noteRepository = get(),
                noteId = noteId
            )
        }

        single { NoteEditor(
            noteRepository = get()
        ) }

        single<NoteRepository> {
            FirebaseNoteRepository(get())
        }

        single { FirebaseFacade() }
    }
