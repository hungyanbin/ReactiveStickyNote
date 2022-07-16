package com.yanbin.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import io.reactivex.rxjava3.core.Observable

@Composable
fun <R, T : R> Observable<T>.subscribeBy(
    onNext: (T) -> Unit = {},
    onError: (Throwable) -> Unit = {},
    onComplete: () -> Unit = {},
) {
    DisposableEffect(this) {
        val disposable = subscribe(onNext, onError, onComplete)
        onDispose { disposable.dispose() }
    }
}
