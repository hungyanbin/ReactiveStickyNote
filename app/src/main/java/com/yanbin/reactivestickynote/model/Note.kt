package com.yanbin.reactivestickynote.model

data class Note(
    val id: String,
    val text: String,
    val position: Position,
    val color: YBColor)
