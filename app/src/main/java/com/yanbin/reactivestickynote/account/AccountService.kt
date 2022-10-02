package com.yanbin.reactivestickynote.account

import io.reactivex.rxjava3.core.Single

interface AccountService {

    fun createAccount(name: String): Single<Account>

    fun getCurrentAccount(): Account

    fun hasAccount(): Boolean
}