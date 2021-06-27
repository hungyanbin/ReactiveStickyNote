package com.yanbin.reactivestickynote.di

import com.yanbin.reactivestickynote.data.FirebaseFacade
import com.yanbin.reactivestickynote.data.FirebaseNoteRepository
import com.yanbin.reactivestickynote.data.InMemoryNoteRepository
import com.yanbin.reactivestickynote.data.NoteRepository
import com.yanbin.reactivestickynote.domain.EditorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun getNoteModule() =
    module {
        viewModel {
            EditorViewModel(get())
        }

        single<NoteRepository> {
            FirebaseNoteRepository(get())
        }

        single { FirebaseFacade() }
    }
