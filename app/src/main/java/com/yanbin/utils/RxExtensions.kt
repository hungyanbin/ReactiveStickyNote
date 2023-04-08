package com.yanbin.utils

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

fun <T> Observable<T>.fromIO(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
}

fun <T> Single<T>.fromIO(): Single<T> {
    return this.subscribeOn(Schedulers.io())
}

fun <T> Observable<T>.toIO(): Observable<T> {
    return this.observeOn(Schedulers.io())
}

fun <T> Observable<T>.fromComputation(): Observable<T> {
    return this.subscribeOn(Schedulers.computation())
}

fun <T> Observable<T>.toMain(): Observable<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
}

inline fun <reified T> Observable<in T>.filterInstance(): Observable<T> {
    return filter { it is T }
        .map { it as T }
}

fun <T> Maybe<T>.toIO(): Maybe<T> {
    return this.observeOn(Schedulers.io())
}

fun <T, R> Optional<T>.fold(someFun: (T) -> R, emptyFun: () -> R): R {
    return if (this.isPresent) {
        someFun(this.get())
    } else {
        emptyFun()
    }
}

fun <T: Any> T.toOptional(): Optional<T> {
    return Optional.of(this)
}
