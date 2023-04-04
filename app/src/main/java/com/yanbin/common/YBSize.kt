package com.yanbin.common

import kotlinx.serialization.Serializable

@Serializable
data class YBSize(
    val width: Float,
    val height: Float
)