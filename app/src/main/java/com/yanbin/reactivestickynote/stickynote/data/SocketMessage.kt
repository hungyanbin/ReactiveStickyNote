package com.yanbin.reactivestickynote.stickynote.data

import com.yanbin.common.YBPointF
import kotlinx.serialization.Serializable

@Serializable
class SocketMessage(
    val type: Type,
    val objectId: String,
    // TODO replace with a wrapper class
    val content: Pair<Attribute, YBPointF>? = null,
) {
    enum class Type {
        Query, Update
    }

    enum class Attribute {
        NewPosition, NewSize
    }

}