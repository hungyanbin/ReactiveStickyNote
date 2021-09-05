package com.yanbin.reactivestickynote.model

import java.util.*
import kotlin.random.Random

data class Note(
    val id: String,
    val text: String,
    val position: Position,
    val color: YBColor) {

    companion object {
        fun createRandomNote(): Note {
            val randomColorIndex = Random.nextInt(YBColor.defaultColors.size)
            val randomPosition = Position(Random.nextInt(-50, 50).toFloat(), Random.nextInt(-50, 50).toFloat())
            val randomId = UUID.randomUUID().toString()
            return Note(randomId, "Hello", randomPosition, YBColor.defaultColors[randomColorIndex])
        }

        fun createEmptyNote(id: String): Note {
            return Note(id, "", Position(0f, Float.MAX_VALUE), YBColor.HotPink)
        }
    }
}
