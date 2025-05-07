package com.apptolast.lifetimejournal.di

import com.apptolast.lifetimejournal.data.repositories.AuthRepository
import com.apptolast.lifetimejournal.data.repositories.AuthRepositoryImpl
import com.apptolast.lifetimejournal.data.repositories.JournalRepository
import com.apptolast.lifetimejournal.data.repositories.JournalRepositoryImpl
import com.apptolast.lifetimejournal.database.getJournalDao
import com.apptolast.lifetimejournal.database.getJournalEntryDao
import com.apptolast.lifetimejournal.database.getRoomDatabase
import com.sunildhiman90.kmauth.google.KMAuthGoogle
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val sharedModule = module {
//    factory { Firebase.auth }
//    factory{ GetSignInWithGoogleOption.Builder(BuildConfig.WEB_ID_CLIENT).build()}
    factory { KMAuthGoogle.googleAuthManager }
    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
    singleOf(::JournalRepositoryImpl) { bind<JournalRepository>() }

//    single<JournalRepository> { JournalRepositoryImpl(get(), get()) }
}

expect val platformModule: Module

//val provideDatabaseModule = module {
//    single { getRoomDatabase(get()) }
////    single { ::getRoomDatabase }
//    single { getJournalDao(get()) }
////    single { ::getJournalDao }
//    single { getJournalEntryDao(get()) }
////    single { ::getJournalEntryDao }
//}
//
///**
// * Módulo para la inyección de dependencias de la base de datos.
// */
val databaseModule = module {

    // Proporciona DAOs
//    single { get<AppDatabase>().journalDao() }
//    single { get<AppDatabase>().journalEntryDao() }

    single { getRoomDatabase(get()) }
    single { getJournalDao(get()) }
    single { getJournalEntryDao(get()) }
}

//expect val sharedNativeModule :Module

