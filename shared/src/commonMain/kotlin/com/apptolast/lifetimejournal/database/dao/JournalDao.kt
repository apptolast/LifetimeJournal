package com.apptolast.lifetimejournal.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.apptolast.lifetimejournal.TABLE_JOURNAL
import com.apptolast.lifetimejournal.TABLE_JOURNAL_ENTRY
import com.apptolast.lifetimejournal.data.datamodel.Journal
import com.apptolast.lifetimejournal.database.entities.JournalEntity
import com.apptolast.lifetimejournal.database.entities.JournalEntryEntity
import com.apptolast.lifetimejournal.database.entities.JournalWithEntries
import com.apptolast.lifetimejournal.database.entities.toJournalEntity
import com.apptolast.lifetimejournal.database.entities.toJournalEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournal(journal: JournalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntries(entries: List<JournalEntryEntity>)

    @Update
    suspend fun updateJournal(journal: JournalEntity)

    @Delete
    suspend fun deleteJournal(journal: JournalEntity)

    @Transaction
    @Query("SELECT * FROM $TABLE_JOURNAL WHERE id = :journalId")
    fun getJournalWithEntries(journalId: String): Flow<JournalWithEntries?>

    @Transaction
    @Query("SELECT * FROM $TABLE_JOURNAL")
    fun getAllJournalsWithEntries(): Flow<List<JournalWithEntries>>

    @Query("DELETE FROM $TABLE_JOURNAL_ENTRY WHERE journalId = :journalId")
    suspend fun deleteEntriesForJournal(journalId: String)

    // Metodo de conveniencia para insertar/actualizar un diario con sus entradas
    @Transaction
    suspend fun upsertJournalWithEntries(journal: Journal) {
        insertJournal(journal.toJournalEntity())
        // Borra las antiguas y inserta las nuevas (o haz un diff más complejo)
        deleteEntriesForJournal(journal.id)
        if (journal.entries.isNotEmpty()) {
            insertEntries(journal.entries.map { it.toJournalEntryEntity() })
        }
    }
}
