package com.yanbin.reactivestickynote

import android.app.Application
import com.yanbin.reactivestickynote.di.getLoginModule
import com.yanbin.reactivestickynote.di.getNoteModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class NoteApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            //inject Android context
            androidContext(this@NoteApplication)
            // use modules
            modules(
                getNoteModule(),
                getLoginModule()
            )
        }
    }
}