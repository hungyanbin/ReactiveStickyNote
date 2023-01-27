package com.yanbin.reactivestickynote.di

import com.yanbin.reactivestickynote.account.AccountService
import com.yanbin.reactivestickynote.account.SPAccountService
import com.yanbin.reactivestickynote.stickynote.data.FirebaseNoteRepository
import com.yanbin.reactivestickynote.stickynote.data.NoteRepository
import com.yanbin.reactivestickynote.editor.vm.ContextMenuViewModel
import com.yanbin.reactivestickynote.edittext.EditTextViewModel
import com.yanbin.reactivestickynote.editor.vm.EditorViewModel
import com.yanbin.reactivestickynote.editor.domain.Editor
import com.yanbin.reactivestickynote.login.LoginViewModel
import com.yanbin.reactivestickynote.editor.vm.StickyNoteViewModel
import com.yanbin.reactivestickynote.editor.vm.ViewPortViewModel
import com.yanbin.reactivestickynote.stickynote.data.LocalHostNoteRepository
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
                editor = get()
            )
        }

        viewModel {
            ContextMenuViewModel(
                contextMenu = get<Editor>().contextMenu
            )
        }

        viewModel {
            ViewPortViewModel(
                viewPort = get<Editor>().viewPort
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
            noteRepository = get(),
            accountService = get()
        ) }

        viewModel {
            StickyNoteViewModel(
                editor = get(),
                accountService = get()
            )
        }

        single<NoteRepository> {
//            FirebaseNoteRepository()
            LocalHostNoteRepository()
        }
    }
