package com.yanbin.utils

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

fun <T> Observable<T>.fromIO(): Observable<T> {
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