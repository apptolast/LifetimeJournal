package com.apptolast.lifetimejournal.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.apptolast.lifetimejournal.COLUMN_NAME_DATE
import com.apptolast.lifetimejournal.COLUMN_NAME_JOURNAL_ENTRY_ID
import com.apptolast.lifetimejournal.COLUMN_NAME_JOURNAL_ID
import com.apptolast.lifetimejournal.TABLE_JOURNAL_ENTRY
import com.apptolast.lifetimejournal.database.entities.JournalEntryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface JournalEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: JournalEntryEntity): Long

    @Update
    suspend fun updateEntry(entry: JournalEntryEntity)

    @Delete
    suspend fun deleteEntry(entry: JournalEntryEntity)

    @Query("SELECT * FROM $TABLE_JOURNAL_ENTRY WHERE $COLUMN_NAME_JOURNAL_ENTRY_ID = :entryId")
    suspend fun getEntryById(entryId: String?): JournalEntryEntity?

    @Query("SELECT * FROM $TABLE_JOURNAL_ENTRY WHERE $COLUMN_NAME_JOURNAL_ENTRY_ID = :journalId")
    suspend fun getEntryByJournalId(journalId: String): JournalEntryEntity?

    @Query("SELECT * FROM $TABLE_JOURNAL_ENTRY")
    fun getAllEntries(): Flow<List<JournalEntryEntity>>

    @Query("SELECT * FROM $TABLE_JOURNAL_ENTRY WHERE $COLUMN_NAME_JOURNAL_ID = :journalId")
    fun getEntriesByJournalId(journalId: Int): Flow<List<JournalEntryEntity>>

    @Query("SELECT * FROM $TABLE_JOURNAL_ENTRY WHERE $COLUMN_NAME_DATE = :date")
    fun getEntriesByDate(date: LocalDate): Flow<List<JournalEntryEntity>>

    @Query("SELECT * FROM $TABLE_JOURNAL_ENTRY WHERE $COLUMN_NAME_JOURNAL_ID = :journalId AND $COLUMN_NAME_DATE = :date")
    fun getEntriesByJournalIdAndDate(journalId: Int, date: LocalDate): Flow<List<JournalEntryEntity>>

    @Query("DELETE FROM $TABLE_JOURNAL_ENTRY WHERE $COLUMN_NAME_JOURNAL_ID = :journalId")
    suspend fun deleteEntriesByJournalId(journalId: Int)

    @Query("DELETE FROM $TABLE_JOURNAL_ENTRY WHERE $COLUMN_NAME_JOURNAL_ID = :journalId")
    suspend fun deleteEntriesByJournalFirestoreId(journalId: String)

    @Query("DELETE FROM $TABLE_JOURNAL_ENTRY")
    suspend fun deleteAllEntries()

    @Query("DELETE FROM $TABLE_JOURNAL_ENTRY WHERE $COLUMN_NAME_JOURNAL_ID = :journalId")
    suspend fun deleteEntryByFirestoreId(journalId: String)

}
