package com.apptolast.lifetimejournal.data.repositories

import com.apptolast.lifetimejournal.data.datamodel.Journal
import com.apptolast.lifetimejournal.data.datamodel.JournalEntry
import com.apptolast.lifetimejournal.database.dao.JournalDao
import com.apptolast.lifetimejournal.database.dao.JournalEntryDao
import com.apptolast.lifetimejournal.database.entities.JournalEntryEntity
import com.apptolast.lifetimejournal.database.entities.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirestoreRoomSynchronizer(
    private val firestoreRepository: FirestoreRepository,
    private val journalDao: JournalDao,
    private val entryDao: JournalEntryDao,
) {
    private val syncScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        // Start listening for Firestore changes and sync them to Room
        setupJournalSynchronization()
        setupEntrySynchronization()
    }

    private fun setupJournalSynchronization() {
        syncScope.launch {
            firestoreRepository.listenToJournalChanges().collectLatest { event ->
                when (event) {
                    is FirestoreEvent.Added -> handleJournalAdded(event.data)
                    is FirestoreEvent.Modified -> handleJournalModified(event.data)
                    is FirestoreEvent.Removed -> handleJournalRemoved(event.data)
                }
            }
        }
    }

    private fun setupEntrySynchronization() {
        syncScope.launch {
            firestoreRepository.listenToEntryChanges().collectLatest { event ->
                when (event) {
                    is FirestoreEvent.Added -> handleEntryAdded(event.data)
                    is FirestoreEvent.Modified -> handleEntryModified(event.data)
                    is FirestoreEvent.Removed -> handleEntryRemoved(event.data)
                }
            }
        }
    }

    suspend fun syncAll() = withContext(Dispatchers.IO) {
        // Fetch all journals and entries from Firestore and sync to Room
        val journals = firestoreRepository.getAllJournals()

        // Clear Room database
//        journalDao.deleteAllJournals()
//        entryDao.deleteAllEntries()

        // Insert all journals
        journals.forEach { journal ->
            val journalEntity = journal.toEntity()
//            val journalDatabaseId = journalDao.insertJournal(journalEntity).toInt()

            // Fetch and insert all entries for this journal
            val journalId = journal.journalId ?: return@forEach
            val entries = firestoreRepository.getEntriesByJournalId(journalId)

            entries.forEach { entry ->
                val entryEntity = entry.toEntity(journalId)
                entryDao.insertEntry(entryEntity)
            }
        }
    }

    private suspend fun handleJournalAdded(journal: Journal) = withContext(Dispatchers.IO) {
        val firestoreId = journal.journalId ?: return@withContext

        // Check if journal already exists in Room
        val existingJournal = journalDao.getJournalById(firestoreId)
        if (existingJournal != null) {
            // Update the existing journal
            val updatedEntity = journal.copy(id = existingJournal.id.toLong()).toEntity()
            journalDao.updateJournal(updatedEntity)
        } else {
            // Insert new journal
            val journalEntity = journal.toEntity()
            journalDao.insertJournal(journalEntity)
        }
    }

    private suspend fun handleJournalModified(journal: Journal) = withContext(Dispatchers.IO) {
        val firestoreId = journal.journalId ?: return@withContext

        // Find the existing journal in Room
        val existingJournal = journalDao.getJournalById(firestoreId)
        if (existingJournal != null) {
            // Update with keeping the local Room ID
            val updatedEntity = journal.copy(id = existingJournal.id.toLong()).toEntity()
            journalDao.updateJournal(updatedEntity)
        } else {
            // If not found, insert as new
            val journalEntity = journal.toEntity()
            journalDao.insertJournal(journalEntity)
        }
    }

    private suspend fun handleJournalRemoved(journal: Journal) = withContext(Dispatchers.IO) {
        val firestoreId = journal.journalId ?: return@withContext

        // Delete the journal and its entries from Room
        journalDao.deleteJournalByJournalId(firestoreId)
        entryDao.deleteEntriesByJournalFirestoreId(firestoreId)
    }

    private suspend fun handleEntryAdded(entry: JournalEntry) = withContext(Dispatchers.IO) {
        val journalId = entry.journalId ?: return@withContext
        val journalEntryId = entry.journalEntryId ?: return@withContext

        // Find the journal in Room
        val journal = journalDao.getJournalById(journalEntryId) ?: return@withContext

        // Check if entry already exists
        val existingEntry = entryDao.getEntryByJournalId(journalId)
        if (existingEntry != null) {
            // Update existing entry
            val updatedEntity = JournalEntryEntity(
                id = existingEntry.id,
                journalId = journalId,
                journalEntryId = journalEntryId,
                title = entry.title,
                description = entry.description,
                date = entry.date,
//                journalId = journal.id,
            )
            entryDao.updateEntry(updatedEntity)
        } else {
            // Insert new entry
            val entryEntity = entry.toEntity(journal.journalId)
            val entryId = entryDao.insertEntry(entryEntity).toInt()

            // Update journal's entry list
            val updatedEntryIds = journal.entryIds + entryId
            journalDao.updateJournalEntryIds(journal.id, updatedEntryIds)
        }
    }

    private suspend fun handleEntryModified(entry: JournalEntry) = withContext(Dispatchers.IO) {
        val journalId = entry.journalId ?: return@withContext
        val journalEntryId = entry.journalEntryId ?: return@withContext

        // Find the journal in Room
        val journal = journalDao.getJournalById(journalEntryId) ?: return@withContext

        // Find the existing entry
        val existingEntry = entryDao.getEntryByJournalId(journalId)
        if (existingEntry != null) {
            // Update the entry
            val updatedEntity = JournalEntryEntity(
                id = existingEntry.id,
                journalId = existingEntry.journalId,
                journalEntryId = existingEntry.journalEntryId,
                title = entry.title,
                description = entry.description,
                date = entry.date,
            )
            entryDao.updateEntry(updatedEntity)
        } else {
            // If not found, insert as new
            val entryEntity = entry.toEntity(journal.journalId)
            val entryId = entryDao.insertEntry(entryEntity).toInt()

            // Update journal's entry list
            val updatedEntryIds = journal.entryIds + entryId
            journalDao.updateJournalEntryIds(journal.id, updatedEntryIds)
        }
    }

    private suspend fun handleEntryRemoved(entry: JournalEntry) = withContext(Dispatchers.IO) {
        val firestoreId = entry.journalId ?: return@withContext

        // Find and delete the entry
        val existingEntry = entryDao.getEntryByJournalId(firestoreId) ?: return@withContext

        // Update journal's entry list
        val journal = journalDao.getJournalById(existingEntry.journalId) ?: return@withContext
        val updatedEntryIds = journal.entryIds - existingEntry.id
        journalDao.updateJournalEntryIds(journal.id, updatedEntryIds)

        // Delete the entry
        entryDao.deleteEntry(existingEntry)
    }

    // Helper methods for manual operations (if needed)

    /**
     * Updates both Room and Firestore with a new journal
     */
    suspend fun createJournal(journal: Journal): Pair<Long, String?> = withContext(Dispatchers.IO) {
        // Save to Firestore first
        val firestoreId = firestoreRepository.createJournal(journal)

        // Then save to Room with the Firestore ID
        val journalWithFirestoreId = journal.copy(journalId = firestoreId)
        val roomId = journalDao.insertJournal(journalWithFirestoreId.toEntity())

        return@withContext Pair(roomId, firestoreId)
    }

    /**
     * Updates both Room and Firestore with a new entry
     */
    suspend fun createEntry(journalId: String?, entry: JournalEntry): String? = withContext(Dispatchers.IO) {
//        val roomJournalId = journalId?.toInt() ?: return@withContext Pair(null, null) // Fixme: remove
        val journal = journalDao.getJournalById(journalId) ?: return@withContext null
        val journalId = journal.journalId ?: return@withContext null

        // Save to Firestore first
        val journalEntryId = firestoreRepository.createEntry(journalId, entry)

        // The entry will be added to Room via the Firestore listener
        // but we'll still return the Firestore ID for reference
        return@withContext journalEntryId
    }

    /**
     * Updates a journal in both Room and Firestore
     */
    suspend fun updateJournal(journal: Journal) = withContext(Dispatchers.IO) {
        val firestoreId = journal.journalId
        if (firestoreId != null) {
            firestoreRepository.updateJournal(journal)
        }
        // Room will be updated via the Firestore listener
    }

    /**
     * Updates an entry in both Room and Firestore
     */
    suspend fun updateEntry(entry: JournalEntry) = withContext(Dispatchers.IO) {
        val firestoreId = entry.journalId
        if (firestoreId != null) {
            firestoreRepository.updateEntry(entry)
        }
        // Room will be updated via the Firestore listener
    }

    /**
     * Deletes a journal from both Room and Firestore
     */
    suspend fun deleteJournal(journalId: String?) = withContext(Dispatchers.IO) {
//        val roomId = id?.toInt() ?: return@withContext
        val journal = journalDao.getJournalById(journalId) ?: return@withContext
        val journalId = journal.journalId ?: return@withContext

        // Delete from Firestore
        firestoreRepository.deleteJournal(journalId)
        // Room will be updated via the Firestore listener
    }

    /**
     * Deletes an entry from both Room and Firestore
     */
    suspend fun deleteEntry(entryId: String?) = withContext(Dispatchers.IO) {
//        val roomId = id?.toInt() ?: return@withContext
        val entry = entryDao.getEntryById(entryId) ?: return@withContext
//        val firestoreId = entry.firestoreId ?: return@withContext

        // Delete from Firestore
        firestoreRepository.deleteEntry(entry.journalEntryId!!)
        // Room will be updated via the Firestore listener
    }
}
