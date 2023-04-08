package com.yanbin.reactivestickynote.account

import android.content.Context
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class SPAccountService(
    private val context: Context
): AccountService {

    companion object {
        private const val SP_NAME = "AccountService"
        private const val SP_KEY_ACCOUNT_NAME = "AccountName"
        private const val SP_KEY_ACCOUNT_ID = "AccountId"
    }

    private val sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    private var account: Account? = null

    override suspend fun createAccount(name: String): Account = withContext(Dispatchers.IO) {
        val accountId = UUID.randomUUID().toString()
        sharedPreferences.edit()
            .putString(SP_KEY_ACCOUNT_NAME, name)
            .putString(SP_KEY_ACCOUNT_ID, accountId)
            .apply()

        Account(name, accountId).also { account = it }
    }

    override suspend fun getCurrentAccount(): Account = withContext(Dispatchers.IO) {
        if (account == null) {
            val accountId = sharedPreferences.getString(SP_KEY_ACCOUNT_ID, "") ?: ""
            val name = sharedPreferences.getString(SP_KEY_ACCOUNT_NAME, "") ?: ""
            account = Account(name, accountId)
        }

        account!!
    }

    override suspend fun hasAccount(): Boolean = withContext(Dispatchers.IO) {
        val accountId = sharedPreferences.getString(SP_KEY_ACCOUNT_ID, "") ?: ""
        accountId.isNotEmpty()
    }
}