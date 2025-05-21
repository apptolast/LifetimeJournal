@file:OptIn(DelicateCoroutinesApi::class)

package com.apptolast.lifetimejournal.data.repositories

import com.apptolast.lifetimejournal.data.datamodel.Journal
import com.apptolast.lifetimejournal.data.datamodel.JournalEntry
import dev.gitlive.firebase.firestore.ChangeType
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

interface FirestoreRepository {
    // Journal operations
    suspend fun createJournal(journal: Journal): String?
    suspend fun getJournal(id: String): Journal?
    suspend fun getAllJournals(): List<Journal>
    suspend fun updateJournal(journal: Journal)
    suspend fun deleteJournal(id: String)

    // Listen to changes
    fun listenToJournalChanges(): Flow<FirestoreEvent<Journal>>
    fun listenToEntryChanges(): Flow<FirestoreEvent<JournalEntry>>

    // Entry operations
    suspend fun createEntry(journalId: String, entry: JournalEntry): String?

    //    suspend fun getEntry(id: String): JournalEntry?
    suspend fun getEntriesByJournalId(journalId: String): List<JournalEntry>
    suspend fun updateEntry(entry: JournalEntry)
    suspend fun deleteEntry(id: String)
}

class FirestoreRepositoryImpl(
    firestore: FirebaseFirestore,
) : FirestoreRepository {

    private val journalsCollection = firestore.collection("journals")
    private val entriesCollection = firestore.collection("entries")

    private val journalChanges = MutableSharedFlow<FirestoreEvent<Journal>>(extraBufferCapacity = 100)
    private val entryChanges = MutableSharedFlow<FirestoreEvent<JournalEntry>>(extraBufferCapacity = 100)

    init {
        setupJournalListener()
        setupEntryListener()
    }

    private fun setupJournalListener() {
        GlobalScope.launch {
            journalsCollection.snapshots().collect { snapshot ->
                snapshot.documentChanges.forEach { change ->
                    val journal = change.document.data<JournalFirestoreModel>().toDomain()
                    when (change.type) {
                        ChangeType.ADDED ->
                            journalChanges.emit(FirestoreEvent.Added(journal))

                        ChangeType.MODIFIED ->
                            journalChanges.emit(FirestoreEvent.Modified(journal))

                        ChangeType.REMOVED ->
                            journalChanges.emit(FirestoreEvent.Removed(journal))

                        else ->
                            println("Unknown change type: ${change.type}")
                    }
                }
            }
        }
    }

    private fun setupEntryListener() {
        GlobalScope.launch {
            entriesCollection.snapshots().collect { snapshot ->
                snapshot.documentChanges.forEach { change ->
                    val entry = change.document.data<EntryFirestoreModel>().toDomain()
                    when (change.type) {
                        ChangeType.ADDED ->
                            entryChanges.emit(FirestoreEvent.Added(entry))

                        ChangeType.MODIFIED ->
                            entryChanges.emit(FirestoreEvent.Modified(entry))

                        ChangeType.REMOVED ->
                            entryChanges.emit(FirestoreEvent.Removed(entry))

                        else ->
                            println("Unknown change type: ${change.type}")
                    }
                }
            }
        }
    }

    override suspend fun createJournal(journal: Journal): String? {
        val journalModel = journal.toFirestoreModel()
        val docRef = journalsCollection.add(journalModel)
        return docRef.id
    }

    override suspend fun getJournal(id: String): Journal? {
        return try {
            val document = journalsCollection.document(id).get()
            document.data<JournalFirestoreModel>().toDomain()
        } catch (e: FirebaseFirestoreException) {
            println(e.message)
            null
        }
    }

    override suspend fun getAllJournals(): List<Journal> {
        return journalsCollection.get().documents.mapNotNull {
            try {
                it.data<JournalFirestoreModel>().toDomain()
            } catch (e: Exception) {
                println(e.message)
                null
            }
        }
    }

    override suspend fun updateJournal(journal: Journal) {
        val firestoreId = journal.journalId ?: return
        val journalModel = journal.toFirestoreModel()
        journalsCollection.document(firestoreId).set(journalModel)
    }

    override suspend fun deleteJournal(id: String) {
        journalsCollection.document(id).delete()

        // Delete all entries associated with this journal
        val entriesToDelete = entriesCollection
            .where { "journalId" equalTo id }
            .get()
            .documents

        entriesToDelete.forEach { doc ->
            entriesCollection.document(doc.id).delete()
        }
    }

    override fun listenToJournalChanges(): Flow<FirestoreEvent<Journal>> = journalChanges

    override fun listenToEntryChanges(): Flow<FirestoreEvent<JournalEntry>> = entryChanges

    override suspend fun createEntry(journalId: String, entry: JournalEntry): String {
        val entryModel = entry.toFirestoreModel(journalId)
        val docRef = entriesCollection.add(entryModel)
        return docRef.id
    }

//    override suspend fun getEntry(id: String): JournalEntry? {
//        return try {
//            val document = entriesCollection.document(id).get()
//            document.data<EntryFirestoreModel>().toDomain()
//        } catch (e: FirebaseFirestoreException) {
//            print(e.message)
//            null
//        }
//    }

    override suspend fun getEntriesByJournalId(journalId: String): List<JournalEntry> {
        return entriesCollection
            .where { "journalId" equalTo journalId }
            .get()
            .documents
            .mapNotNull {
                try {
                    it.data<EntryFirestoreModel>().toDomain()
                } catch (e: Exception) {
                    print(e.message)
                    null
                }
            }
    }

    override suspend fun updateEntry(entry: JournalEntry) {
        val journalId = entry.journalId ?: return
        val journalEntryId = entry.journalEntryId ?: return
        val entryModel = entry.toFirestoreModel(journalId)
        entriesCollection.document(journalEntryId).set(entryModel)
    }

    override suspend fun deleteEntry(id: String) {
        entriesCollection.document(id).delete()
    }
}

sealed class FirestoreEvent<out T> {
    data class Added<T>(val data: T) : FirestoreEvent<T>()
    data class Modified<T>(val data: T) : FirestoreEvent<T>()
    data class Removed<T>(val data: T) : FirestoreEvent<T>()
}

// Firestore models
@Serializable
data class JournalFirestoreModel(
    val id: String? = null,
    val title: String = "",
    val description: String = "",
    val cover: String = "",
    val entryIds: List<String> = emptyList(),
)

@Serializable
data class EntryFirestoreModel(
    val id: Long? = null,
    val journalId: String = "",
    val journalEntryId: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "", // ISO formatted date
)

// Extensions for domain model conversion
fun Journal.toFirestoreModel(): JournalFirestoreModel {
    return JournalFirestoreModel(
        id = this.journalId,
        title = this.title,
        description = this.description,
        cover = this.cover,
        entryIds = this.entries.mapNotNull { it.journalId },
    )
}

fun JournalFirestoreModel.toDomain(): Journal {
    return Journal(
        id = null, // Local DB ID will be filled by the sync mechanism
        journalId = this.id,
        title = this.title,
        description = this.description,
        cover = this.cover,
        entries = mutableListOf(), // Entries will be loaded separately
    )
}

fun JournalEntry.toFirestoreModel(journalId: String): EntryFirestoreModel {
//fun JournalEntry.toFirestoreModel(): EntryFirestoreModel {
    return EntryFirestoreModel(
        id = this.id ?: -1,
        journalId = journalId,
        title = this.title,
        description = this.description,
        date = this.date.toString(), // Convert LocalDate to String
    )
}

fun EntryFirestoreModel.toDomain(): JournalEntry {
    return JournalEntry(
        id = null, // Local DB ID will be filled by the sync mechanism
        journalId = this.journalId,
        journalEntryId = this.journalId,
        title = this.title,
        description = this.description,
        date = LocalDate.parse(this.date), // Parse String back to LocalDate
    )
}
