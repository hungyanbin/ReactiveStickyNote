package com.yanbin.reactivestickynote.stickynote.data

import com.yanbin.common.YBPointF
import kotlinx.serialization.Serializable

@Serializable
data class UpdatedAttributes(
    val objectId: String,
    val position: YBPointF
)