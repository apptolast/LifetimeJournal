package com.apptolast.lifetimejournal.data.repositories

import com.apptolast.lifetimejournal.data.datamodel.Journal
import com.apptolast.lifetimejournal.data.datamodel.JournalEntry
import com.apptolast.lifetimejournal.database.dao.JournalDao
import com.apptolast.lifetimejournal.database.dao.JournalEntryDao
import com.apptolast.lifetimejournal.database.entities.toDomain
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate

/**
 * Implementation of JournalRepository that uses Room as a cache and Firestore as the source of truth.
 * All operations are performed on Firestore first, and Room is updated via the synchronizer.
 */
class JournalRepositoryImpl(
    private val journalDao: JournalDao,
    private val entryDao: JournalEntryDao,
    private val firestore: FirebaseFirestore,
) : JournalRepository {

    private val firestoreRepository = FirestoreRepositoryImpl(firestore)
    private val synchronizer = FirestoreRoomSynchronizer(firestoreRepository, journalDao, entryDao)

    /**
     * Initializes the repository by syncing all data from Firestore to Room
     */
    suspend fun initialize() {
        synchronizer.syncAll()
    }

    override suspend fun createJournal(journal: Journal): Long = withContext(Dispatchers.IO) {
        val (roomId, _) = synchronizer.createJournal(journal)
        return@withContext roomId
    }

    override suspend fun getJournal(id: Long?): Journal? = withContext(Dispatchers.IO) {
        val journalId = id?.toInt() ?: return@withContext null
        val journalWithEntries = journalDao.getJournalWithEntries(journalId)
        return@withContext journalWithEntries?.toDomain()
    }

    override fun getAllJournals(): Flow<List<Journal>> {
        return journalDao.getAllJournalsWithEntries().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun updateJournal(journal: Journal) = withContext(Dispatchers.IO) {
        synchronizer.updateJournal(journal)
    }

    override suspend fun deleteJournal(id: String?) = withContext(Dispatchers.IO) {
        synchronizer.deleteJournal(id)
    }

    override suspend fun createEntry(journalId: String?, entry: JournalEntry): String? = withContext(Dispatchers.IO) {
        val journalId = synchronizer.createEntry(journalId, entry)
        return@withContext journalId
    }

    override suspend fun getEntry(entryId: String?): JournalEntry? = withContext(Dispatchers.IO) {
//        val entryId = id?.toInt() ?: return@withContext null
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
        synchronizer.updateEntry(entry)
    }

    override suspend fun deleteEntry(id: String?) = withContext(Dispatchers.IO) {
        synchronizer.deleteEntry(id)
    }

    override suspend fun addEntryToJournal(journalId: String?, entry: JournalEntry): String? {
        return createEntry(journalId, entry)
    }

    override suspend fun removeEntryFromJournal( entryId: String?) = withContext(Dispatchers.IO) {
        // This operation is handled implicitly by the deletion of the entry
        deleteEntry(entryId)
    }
}
