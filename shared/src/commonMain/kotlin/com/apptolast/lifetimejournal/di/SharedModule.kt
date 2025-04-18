package com.apptolast.lifetimejournal.di

import com.apptolast.lifetimejournal.repositories.AuthRepository
import com.apptolast.lifetimejournal.repositories.AuthRepositoryImpl
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val sharedModule = module {
    factory { Firebase.auth }
    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
}

