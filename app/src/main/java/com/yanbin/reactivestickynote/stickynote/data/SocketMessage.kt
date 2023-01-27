package com.yanbin.reactivestickynote.stickynote.data

import kotlinx.serialization.Serializable

@Serializable
class SocketMessage(
    val type: Type,
    val objectId: String,
) {
    enum class Type {
        Query, Update
    }
}