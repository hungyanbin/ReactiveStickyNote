package com.yanbin.reactivestickynote.editor.model

import java.util.*
import kotlin.random.Random

data class StickyNote(
    val id: String,
    val text: String,
    val position: Position,
    val color: YBColor) {

    companion object {
        fun createRandomNote(): StickyNote {
            val randomColorIndex = Random.nextInt(YBColor.defaultColors.size)
            val randomPosition = Position(Random.nextInt(-50, 50).toFloat(), Random.nextInt(-50, 50).toFloat())
            val randomId = UUID.randomUUID().toString()
            return StickyNote(randomId, "Hello", randomPosition, YBColor.defaultColors[randomColorIndex])
        }

        fun createEmptyNote(id: String): StickyNote {
            return StickyNote(id, "", Position(0f, Float.MAX_VALUE), YBColor.HotPink)
        }
    }
}
