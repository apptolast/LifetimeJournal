package com.apptolast.lifetimejournal.di

import com.apptolast.lifetimejournal.BuildConfig
import io.kotzilla.sdk.analytics.koin.analytics
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
//    single(named("testApiKey")) { BuildConfig.TEST_API_KEY }
//    factory { Greeting() }
//    factory { Firebase.auth }
}

val viewModelsModule = module {
//    viewModelOf(::HomeListViewModel)
}

// expect val nativeModule: Module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(appModule, viewModelsModule)

    // Kotzilla
    analytics {
        setApiKey(BuildConfig.KOTZILLA_API_KEY) // Available in the kotzilla.json file
        setVersion("0.1.0")
    }
}

// called by iOS
fun initKoinIos() = initKoin {}
