package com.yanbin.reactivestickynote.account

interface AccountService {

    suspend fun createAccount(name: String): Account

    suspend fun getCurrentAccount(): Account

    suspend fun hasAccount(): Boolean
}