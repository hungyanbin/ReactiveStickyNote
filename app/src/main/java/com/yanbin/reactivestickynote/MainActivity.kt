package com.yanbin.reactivestickynote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import com.yanbin.reactivestickynote.domain.EditorViewModel
import com.yanbin.reactivestickynote.ui.screen.EditorScreen
import com.yanbin.reactivestickynote.ui.theme.ReactiveStickyNoteTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReactiveStickyNoteTheme {
                val viewModel by viewModel<EditorViewModel>()
                EditorScreen(viewModel = viewModel)
            }
        }
    }
}

