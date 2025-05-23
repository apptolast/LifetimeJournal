package com.apptolast.lifetimejournal.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.apptolast.lifetimejournal.TABLE_JOURNAL_ENTRY
import com.apptolast.lifetimejournal.database.entities.JournalEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: JournalEntryEntity)

    @Update
    suspend fun updateEntry(entry: JournalEntryEntity)

    @Delete
    suspend fun deleteEntry(entry: JournalEntryEntity)

    @Query("SELECT * FROM $TABLE_JOURNAL_ENTRY WHERE id = :entryId")
    fun getEntryById(entryId: String): Flow<JournalEntryEntity?>
}
