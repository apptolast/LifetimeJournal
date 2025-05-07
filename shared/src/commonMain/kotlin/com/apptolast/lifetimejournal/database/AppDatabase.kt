package com.apptolast.lifetimejournal.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.apptolast.lifetimejournal.database.dao.JournalDao
import com.apptolast.lifetimejournal.database.dao.JournalEntryDao
import com.apptolast.lifetimejournal.database.entities.JournalEntity
import com.apptolast.lifetimejournal.database.entities.JournalEntryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [JournalEntity::class, JournalEntryEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
//    abstract fun journalDao(): JournalDao
//    abstract fun journalEntryDao(): JournalEntryDao

    abstract fun getJournalDao(): JournalDao
    abstract fun getJournalEntryDao(): JournalEntryDao
}

//@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "KotlinNoActualForExpect")
//expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>
//expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>

// The Room compiler generates the `actual` implementations.
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

//@Suppress("NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "KotlinNoActualForExpect")
//expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>

//fun createDatabase(): AppDatabase {
//    return Room.inMemoryDatabaseBuilder<AppDatabase>(
//        factory = AppDatabaseConstructor::initialize
//    ).build()
//}


fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>,
): AppDatabase {
    return builder
        .addMigrations()
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

fun getJournalDao(appDatabase: AppDatabase): JournalDao = appDatabase.getJournalDao()
fun getJournalEntryDao(appDatabase: AppDatabase): JournalEntryDao = appDatabase.getJournalEntryDao()
