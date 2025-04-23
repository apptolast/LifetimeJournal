package com.apptolast.lifetimejournal

import android.app.Application
import com.apptolast.lifetimejournal.di.initKoin
import com.sunildhiman90.kmauth.core.KMAuthInitializer
import com.sunildhiman90.kmauth.core.KMAuthPlatformContext
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            KMAuthInitializer.initWithContext(
                webClientId = BuildConfig.WEB_ID_CLIENT,
                kmAuthPlatformContext = KMAuthPlatformContext(this@MyApplication),
            )

            androidLogger()
            androidContext(this@MyApplication)
        }
    }
}
