package com.yanbin.reactivestickynote.di

import com.yanbin.reactivestickynote.account.AccountService
import com.yanbin.reactivestickynote.account.SPAccountService
import com.yanbin.reactivestickynote.editor.data.FirebaseNoteRepository
import com.yanbin.reactivestickynote.editor.data.NoteRepository
import com.yanbin.reactivestickynote.editor.vm.ContextMenuViewModel
import com.yanbin.reactivestickynote.edittext.EditTextViewModel
import com.yanbin.reactivestickynote.editor.vm.EditorViewModel
import com.yanbin.reactivestickynote.editor.domain.StickyNoteEditor
import com.yanbin.reactivestickynote.login.LoginViewModel
import com.yanbin.reactivestickynote.editor.vm.StickyNoteViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun getLoginModule() = module {

    viewModel {
        LoginViewModel(
            accountService = get()
        )
    }

    single<AccountService> {
        SPAccountService(androidContext())
    }
}

fun getNoteModule() =
    module {
        viewModel {
            EditorViewModel(
                stickyNoteEditor = get(),
                noteRepository = get()
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
            noteRepository = get(),
            accountService = get()
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
