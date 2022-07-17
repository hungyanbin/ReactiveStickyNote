package com.yanbin.reactivestickynote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yanbin.reactivestickynote.domain.EditTextViewModel
import com.yanbin.reactivestickynote.domain.EditorViewModel
import com.yanbin.reactivestickynote.ui.screen.EditTextScreen
import com.yanbin.reactivestickynote.ui.screen.EditorScreen
import com.yanbin.reactivestickynote.ui.screen.Screen
import com.yanbin.reactivestickynote.ui.theme.ReactiveStickyNoteTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            ReactiveStickyNoteTheme {
                NavHost(navController, startDestination = Screen.Editor.route) {
                    composable(Screen.Editor.route) {
                        val viewModel by viewModel<EditorViewModel>()
                        EditorScreen(
                            viewModel = viewModel,
                            openEditTextScreen = { note ->
                                navController.navigate(Screen.EditText.buildRoute(note.id, note.text))
                            }
                        )
                    }

                    composable(
                        Screen.EditText.route
                    ) { backStackEntry ->
                        val viewModel by backStackEntry.viewModel<EditTextViewModel> {
                            parametersOf(
                                backStackEntry.arguments?.getString(Screen.EditText.KEY_NOTE_ID),
                                backStackEntry.arguments?.getString(Screen.EditText.KEY_DEFAULT_TEXT),
                            )
                        }
                        EditTextScreen(viewModel, onLeaveScreen = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}

