package com.yanbin.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import java.util.*

fun <T, R> Flow<Optional<T>>.mapOptional(mapper: (T) -> R): Flow<R> {
    return this.filter { it.isPresent }
        .map { mapper(it.get()) }
}