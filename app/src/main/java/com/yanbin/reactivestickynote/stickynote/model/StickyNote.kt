package com.yanbin.reactivestickynote.stickynote.model

import com.yanbin.common.YBSize
import java.util.*
import kotlin.random.Random

data class StickyNote(
    val id: String,
    val text: String,
    val position: Position,
    val size: YBSize,
    val color: YBColor) {

    companion object {
        fun createRandomNote(): StickyNote {
            val randomColorIndex = Random.nextInt(YBColor.defaultColors.size)
            val randomPosition = Position(Random.nextInt(-50, 50).toFloat(), Random.nextInt(-50, 50).toFloat())
            val size = YBSize(DEFAULT_SIZE, DEFAULT_SIZE)
            val randomId = UUID.randomUUID().toString()
            return StickyNote(randomId, "Hello", randomPosition, size, YBColor.defaultColors[randomColorIndex])
        }

        fun createEmptyNote(id: String): StickyNote {
            return StickyNote(id, "", Position(0f, Float.MAX_VALUE), YBSize(0f, 0f), YBColor.HotPink)
        }

        const val DEFAULT_SIZE = 330f
        const val MIN_SIZE = 250f
    }
}
