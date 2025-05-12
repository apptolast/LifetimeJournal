package com.apptolast.lifetimejournal.di

import com.apptolast.lifetimejournal.BuildConfig
import com.apptolast.lifetimejournal.features.createjournal.presentation.CreateJournalViewModel
import com.apptolast.lifetimejournal.features.entries.presentation.EntriesViewModel
import com.apptolast.lifetimejournal.features.journals.presentation.JournalsViewModel
import com.apptolast.lifetimejournal.features.login.presentation.LoginViewModel
import io.kotzilla.sdk.analytics.koin.analytics
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
//    single(named("testApiKey")) { BuildConfig.TEST_API_KEY }
//    factory { Greeting() }
//    factory { Firebase.auth }
//    factory { KMAuthGoogle.googleAuthManager }
}

val viewModelsModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::JournalsViewModel)
    viewModelOf(::EntriesViewModel)
    viewModelOf(::CreateJournalViewModel)
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(appModule, sharedModule, viewModelsModule, platformModule, databaseModule)

    // Kotzilla
    analytics {
        setApiKey(BuildConfig.KOTZILLA_API_KEY) // Available in the kotzilla.json file
        setVersion("0.1.0")
    }
}

// called by iOS
fun initKoinIos() = initKoin {
//    KMAuthInitializer.init(
//        webClientId = BuildConfig.WEB_ID_CLIENT,
//    )
}
