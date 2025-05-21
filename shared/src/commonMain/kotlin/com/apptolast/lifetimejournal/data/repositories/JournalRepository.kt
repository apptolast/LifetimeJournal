package com.apptolast.lifetimejournal.data.repositories

import com.apptolast.lifetimejournal.data.datamodel.Journal
import com.apptolast.lifetimejournal.data.datamodel.JournalEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface JournalRepository {

    // Journal operations
    suspend fun createJournal(journal: Journal): Long
    suspend fun getJournal(id: Long?): Journal?
    fun getAllJournals(): Flow<List<Journal>>
    suspend fun updateJournal(journal: Journal)
    suspend fun deleteJournal(id: String?)

    // Journal Entry operations
    suspend fun createEntry(journalId: String?, entry: JournalEntry): String?
    suspend fun getEntry(id: String?): JournalEntry?
    fun getEntriesByJournalId(journalId: Long?): Flow<List<JournalEntry>>
    fun getEntriesByDate(date: LocalDate): Flow<List<JournalEntry>>
    fun getEntriesByJournalIdAndDate(journalId: Long?, date: LocalDate): Flow<List<JournalEntry>>
    suspend fun updateEntry(entry: JournalEntry)
    suspend fun deleteEntry(id: String?)

    // Combined operations
    suspend fun addEntryToJournal(journalId: String?, entry: JournalEntry): String?
    suspend fun removeEntryFromJournal(entryId: String?)
}
