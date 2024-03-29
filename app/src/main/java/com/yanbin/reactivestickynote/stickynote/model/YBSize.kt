package com.yanbin.reactivestickynote.stickynote.model

data class YBSize(val width: Float, val height: Float) {

    fun withDelta(widthDelta: Float, heightDelta: Float): YBSize {
        return YBSize(width + widthDelta, height + heightDelta)
    }
}