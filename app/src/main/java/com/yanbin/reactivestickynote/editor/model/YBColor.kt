package com.yanbin.reactivestickynote.editor.model

data class YBColor(
    val color: Long
) {
    companion object {
        val HotPink = YBColor(0xFFFF7EB9)
        val Aquamarine = YBColor(0xFF7AFCFF)
        val PaleCanary = YBColor(0xFFFEFF9C)
        val Gorse = YBColor(0xFFFFF740)

        val defaultColors = listOf(HotPink, Aquamarine, PaleCanary, Gorse)
    }
}