package com.apptolast.lifetimejournal.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.apptolast.lifetimejournal.COLUMN_NAME_ENTRY_IDS
import com.apptolast.lifetimejournal.COLUMN_NAME_ID
import com.apptolast.lifetimejournal.COLUMN_NAME_JOURNAL_ID
import com.apptolast.lifetimejournal.TABLE_JOURNAL
import com.apptolast.lifetimejournal.database.entities.JournalEntity
import com.apptolast.lifetimejournal.database.entities.JournalWithEntriesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournal(journal: JournalEntity): Long

    @Update
    suspend fun updateJournal(journal: JournalEntity)

    @Delete
    suspend fun deleteJournal(journal: JournalEntity)

    @Query("SELECT * FROM $TABLE_JOURNAL WHERE $COLUMN_NAME_ID = :journalId")
    suspend fun getJournalById(journalId: String?): JournalEntity?

    @Query("SELECT * FROM $TABLE_JOURNAL")
    fun getAllJournals(): Flow<List<JournalEntity>>

    @Transaction
    @Query("SELECT * FROM $TABLE_JOURNAL WHERE $COLUMN_NAME_ID = :journalId")
    suspend fun getJournalWithEntries(journalId: Int): JournalWithEntriesEntity?

    @Transaction
    @Query("SELECT * FROM $TABLE_JOURNAL WHERE $COLUMN_NAME_JOURNAL_ID = :journalId")
    suspend fun getJournalWithEntriesByJournalId(journalId: String): JournalWithEntriesEntity?

    @Transaction
    @Query("SELECT * FROM $TABLE_JOURNAL")
    fun getAllJournalsWithEntries(): Flow<List<JournalWithEntriesEntity>>

    @Transaction
    suspend fun insertAndGetJournal(journal: JournalEntity): JournalEntity? {
        val id = insertJournal(journal)
        return getJournalById(journal.journalId) // TODO !!!!!!!!!! journal.journalId vs journal.id
    }

    @Query("UPDATE $TABLE_JOURNAL SET $COLUMN_NAME_ENTRY_IDS = :entryIds WHERE $COLUMN_NAME_ID = :journalId")
    suspend fun updateJournalEntryIds(journalId: Int, entryIds: List<Int>)

    @Query("DELETE FROM $TABLE_JOURNAL")
    suspend fun deleteAllJournals()

    @Query("DELETE FROM $TABLE_JOURNAL WHERE $COLUMN_NAME_JOURNAL_ID = :journalId")
    suspend fun deleteJournalByJournalId(journalId: String)

}
