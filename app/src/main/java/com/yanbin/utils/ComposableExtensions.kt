package com.yanbin.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

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

@Composable
fun <T> Flow<T>.collectOnLifecycleStarted(action: suspend (T) -> Unit) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(key1 = Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@collectOnLifecycleStarted.collect { action(it) }
        }
    }
}
