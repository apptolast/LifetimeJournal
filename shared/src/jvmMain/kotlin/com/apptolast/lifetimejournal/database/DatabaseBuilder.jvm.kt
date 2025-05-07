package com.apptolast.lifetimejournal.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.apptolast.lifetimejournal.DATABASE_NAME
import java.io.File

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), DATABASE_NAME)
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    )
}
