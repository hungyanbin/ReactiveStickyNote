package com.yanbin.reactivestickynote.ui.route

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Editor : Screen("editor")
    object EditText : Screen("editText/{noteId}?defaultText={defaultText}") {
        fun buildRoute(noteId: String, defaultText: String) = "editText/${noteId}?${KEY_DEFAULT_TEXT}=${defaultText}"
        const val KEY_NOTE_ID = "noteId"
        const val KEY_DEFAULT_TEXT = "defaultText"
    }
}