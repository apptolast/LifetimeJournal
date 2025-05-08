package com.apptolast.lifetimejournal.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.apptolast.lifetimejournal.DATABASE_NAME
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

//fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
//    val dbFilePath = NSHomeDirectory() + "/$DATABASE_NAME"
//    return Room.databaseBuilder<AppDatabase>(
//        name = dbFilePath,
//        factory = { AppDatabase::class.instantiateImpl() },
//    ).setDriver(BundledSQLiteDriver())
//}


//private val databaseSchema = AppDatabase::class.qualifiedName!!

//actual object AppDatabaseConstructor : AppDatabaseConstructor {
//    override fun initialize(): AppDatabase {
//        // Implementación para iOS
//        val dbFilePath = NSHomeDirectory() + "/$DATABASE_NAME"
//            return Room.databaseBuilder<AppDatabase>(
//                name = dbFilePath,
//                factory = { AppDatabase::class.instantiateImpl() },
//            ).setDriver(BundledSQLiteDriver())
//    }
//}

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = documentDirectory() + "/$DATABASE_NAME"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}
