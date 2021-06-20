package com.yanbin.reactivestickynote.model

data class Position(val x: Float, val y: Float) {

    operator fun plus(other: Position): Position {
        return Position(x + other.x, y + other.y)
    }
}