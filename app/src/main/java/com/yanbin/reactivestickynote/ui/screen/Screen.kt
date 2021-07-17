package com.yanbin.reactivestickynote.ui.screen

sealed class Screen(val route: String) {
    object Board : Screen("board")
    object EditText : Screen("editText") {
        const val KEY_NOTE_ID = "noteId"
    }
}