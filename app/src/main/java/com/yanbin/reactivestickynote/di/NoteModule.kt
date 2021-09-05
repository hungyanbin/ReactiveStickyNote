package com.yanbin.reactivestickynote.di

import com.yanbin.reactivestickynote.data.FirebaseNoteRepository
import com.yanbin.reactivestickynote.data.NoteRepository
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

        viewModel { (noteId: String, defaultText: String) ->
            EditTextViewModel(
                noteRepository = get(),
                noteId = noteId,
                defaultText = defaultText
            )
        }

        single { NoteEditor(
            noteRepository = get()
        ) }

        single<NoteRepository> {
            FirebaseNoteRepository()
        }
    }
