package com.yanbin.reactivestickynote.di

import com.yanbin.reactivestickynote.domain.EditorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun getNoteModule() =
    module {
        viewModel {
            EditorViewModel()
        }
    }
