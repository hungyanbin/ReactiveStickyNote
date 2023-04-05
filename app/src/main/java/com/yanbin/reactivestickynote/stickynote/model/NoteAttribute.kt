package com.yanbin.reactivestickynote.stickynote.model

import com.yanbin.common.YBSize

sealed class NoteAttribute {
    class Pos(val position: Position) : NoteAttribute()
    class Size(val size: YBSize) : NoteAttribute()
    class Color(val color: YBColor) : NoteAttribute()
    class Text(val text: String) : NoteAttribute()
}