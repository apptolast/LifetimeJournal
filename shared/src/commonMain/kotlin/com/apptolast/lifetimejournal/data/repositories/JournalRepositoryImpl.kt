package com.apptolast.lifetimejournal.data.repositories

import com.apptolast.lifetimejournal.data.datamodel.Journal
import com.apptolast.lifetimejournal.data.datamodel.JournalEntry
import com.apptolast.lifetimejournal.database.dao.JournalDao
import com.apptolast.lifetimejournal.database.dao.JournalEntryDao
import com.apptolast.lifetimejournal.database.entities.JournalEntryEntity
import com.apptolast.lifetimejournal.database.mappers.toDomain
import com.apptolast.lifetimejournal.database.mappers.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate

class JournalRepositoryImpl(
    private val journalDao: JournalDao,
    private val entryDao: JournalEntryDao,
) : JournalRepository/*, KoinComponent */ {

//    private val journalDao: JournalDao by inject()
//    private val entryDao: JournalEntryDao by inject()


    override suspend fun createJournal(journal: Journal): Long = withContext(Dispatchers.IO) {
        val journalId = journalDao.insertJournal(journal.toEntity()).toInt()

        // Insert associated entries if any
        journal.entries.forEach { entry ->
            val entryEntity = entry.toEntity(journalId)
            entryDao.insertEntry(entryEntity)
        }

        return@withContext journalId.toLong()
    }

    override suspend fun getJournal(id: Long?): Journal? = withContext(Dispatchers.IO) {
        val journalId = id ?: return@withContext null
        val journalWithEntries = journalDao.getJournalWithEntries(journalId.toInt())
        return@withContext journalWithEntries?.toDomain()
    }

    override fun getAllJournals(): Flow<List<Journal>> {
        return journalDao.getAllJournalsWithEntries().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun updateJournal(journal: Journal) = withContext(Dispatchers.IO) {
        val journalId = journal.id ?: return@withContext

        // Update the journal entity
        journalDao.updateJournal(journal.toEntity())

        // Handle entries updates by managing their connections to this journal
//        val currentEntries = entryDao.getEntriesByJournalId(journalId.toInt()).map { entries ->
//            entries.map { it.id }
//        }.first()

        val newEntryIds = journal.entries.mapNotNull { it.id?.toInt() }

        // Update the entry IDs list in the journal
        journalDao.updateJournalEntryIds(journalId.toInt(), newEntryIds)
    }

    override suspend fun deleteJournal(id: Long?) = withContext(Dispatchers.IO) {
        val journalId = id?.toInt() ?: return@withContext
        val journal = journalDao.getJournalById(journalId) ?: return@withContext

        // Delete associated entries first
        entryDao.deleteEntriesByJournalId(journalId)

        // Then delete the journal
        journalDao.deleteJournal(journal)
    }

    override suspend fun createEntry(journalId: Long?, entry: JournalEntry): Long? = withContext(Dispatchers.IO) {
        val journalIdInt = journalId?.toInt() ?: return@withContext null

        // Create the entry
        val entryEntity = entry.toEntity(journalIdInt)
        val entryId = entryDao.insertEntry(entryEntity).toInt()

        // Update the journal's entryIds list
        val journal = journalDao.getJournalById(journalIdInt) ?: return@withContext null
        val updatedEntryIds = journal.entryIds + entryId
        journalDao.updateJournalEntryIds(journalIdInt, updatedEntryIds)

        return@withContext entryId.toLong()
    }

    override suspend fun getEntry(id: Long?): JournalEntry? = withContext(Dispatchers.IO) {
        val entryId = id?.toInt() ?: return@withContext null
        val entry = entryDao.getEntryById(entryId)
        return@withContext entry?.toDomain()
    }

    override fun getEntriesByJournalId(journalId: Long?): Flow<List<JournalEntry>> {
        val journalIdInt = journalId?.toInt() ?: return flowOf(emptyList())
        return entryDao.getEntriesByJournalId(journalIdInt).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getEntriesByDate(date: LocalDate): Flow<List<JournalEntry>> {
        return entryDao.getEntriesByDate(date).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getEntriesByJournalIdAndDate(journalId: Long?, date: LocalDate): Flow<List<JournalEntry>> {
        val journalIdInt = journalId?.toInt() ?: return flowOf(emptyList())
        return entryDao.getEntriesByJournalIdAndDate(journalIdInt, date).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun updateEntry(entry: JournalEntry) = withContext(Dispatchers.IO) {
        val entryId = entry.id?.toInt() ?: return@withContext
        val existingEntry = entryDao.getEntryById(entryId) ?: return@withContext

        val updatedEntity = JournalEntryEntity(
            id = entryId,
            title = entry.title,
            description = entry.description,
            date = entry.date,
            journalId = existingEntry.journalId,
        )

        entryDao.updateEntry(updatedEntity)
    }

    override suspend fun deleteEntry(id: Long?) = withContext(Dispatchers.IO) {
        val entryId = id?.toInt() ?: return@withContext
        val entry = entryDao.getEntryById(entryId) ?: return@withContext

        // Also remove from the journal's entry list
        val journal = journalDao.getJournalById(entry.journalId) ?: return@withContext
        val updatedEntryIds = journal.entryIds - entryId
        journalDao.updateJournalEntryIds(entry.journalId, updatedEntryIds)

        // Delete the entry
        entryDao.deleteEntry(entry)
    }

    override suspend fun addEntryToJournal(journalId: Long?, entry: JournalEntry): Long? {
        return createEntry(journalId, entry)
    }

    override suspend fun removeEntryFromJournal(journalId: Long?, entryId: Long?) = withContext(Dispatchers.IO) {
        val journalIdInt = journalId?.toInt() ?: return@withContext
        val entryIdInt = entryId?.toInt() ?: return@withContext

        // Get the journal and update its entry list
        val journal = journalDao.getJournalById(journalIdInt) ?: return@withContext
        val updatedEntryIds = journal.entryIds - entryIdInt
        journalDao.updateJournalEntryIds(journalIdInt, updatedEntryIds)
    }
}
