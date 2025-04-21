package com.apptolast.lifetimejournal

import android.app.Application
import com.apptolast.lifetimejournal.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger()
            androidContext(this@MyApplication)
        }
    }
}
