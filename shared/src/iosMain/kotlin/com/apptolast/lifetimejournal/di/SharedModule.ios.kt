package com.apptolast.lifetimejournal.di

import androidx.room.RoomDatabase
import com.apptolast.lifetimejournal.data.auth.GoogleSignInHelper
import com.apptolast.lifetimejournal.database.AppDatabase
import com.apptolast.lifetimejournal.database.getDatabaseBuilder
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformModule = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        getDatabaseBuilder()
    }
    singleOf(::GoogleSignInHelper)
}
