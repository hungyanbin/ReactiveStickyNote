package com.yanbin.reactivestickynote.ui.screen

sealed class Screen(val route: String) {
    object Board : Screen("board")
    object EditText : Screen("editText/{noteId}?defaultText={defaultText}") {
        fun buildRoute(noteId: String, defaultText: String) = "editText/${noteId}?${KEY_DEFAULT_TEXT}=${defaultText}"
        const val KEY_NOTE_ID = "noteId"
        const val KEY_DEFAULT_TEXT = "defaultText"
    }
}