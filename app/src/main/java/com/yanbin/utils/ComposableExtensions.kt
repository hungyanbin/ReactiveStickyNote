package com.yanbin.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.composable
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
