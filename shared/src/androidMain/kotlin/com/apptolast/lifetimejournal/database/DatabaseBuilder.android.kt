package com.apptolast.lifetimejournal.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.apptolast.lifetimejournal.DATABASE_NAME

fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath(DATABASE_NAME)
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
