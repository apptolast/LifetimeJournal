package com.apptolast.lifetimejournal.di

import androidx.room.RoomDatabase
import com.apptolast.lifetimejournal.database.AppDatabase
import com.apptolast.lifetimejournal.database.getDatabaseBuilder
import org.koin.dsl.module


actual val platformModule = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        getDatabaseBuilder()
    }
}
