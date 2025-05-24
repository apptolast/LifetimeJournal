@file:OptIn(DelicateCoroutinesApi::class)

package com.apptolast.lifetimejournal.data.repositories

import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.serialization.Serializable

interface FirebaseService {
    suspend fun getJournals(): List<FirebaseJournal>
    suspend fun getEntries(journalId: String): List<FirebaseJournalEntry>
    suspend fun saveJournal(journal: FirebaseJournal)
    suspend fun saveEntry(entry: FirebaseJournalEntry) // entry.journalId debe estar presente
    suspend fun deleteJournal(journalId: String)
    suspend fun deleteEntry(journalId: String, entryId: String)
}

class FirestoreRepositoryImpl(
    private val firestore: FirebaseFirestore,
) : FirebaseService {


//    private fun setupJournalListener() {
//        GlobalScope.launch {
//            journalsCollection.snapshots().collect { snapshot ->
//                snapshot.documentChanges.forEach { change ->
//                    val journal = change.document.data<JournalFirestoreModel>().toDomain()
//                    when (change.type) {
//                        ChangeType.ADDED ->
//                            journalChanges.emit(FirestoreEvent.Added(journal))
//
//                        ChangeType.MODIFIED ->
//                            journalChanges.emit(FirestoreEvent.Modified(journal))
//
//                        ChangeType.REMOVED ->
//                            journalChanges.emit(FirestoreEvent.Removed(journal))
//
//                        else ->
//                            println("Unknown change type: ${change.type}")
//                    }
//                }
//            }
//        }
//    }

    //    private fun setupEntryListener() {
//        GlobalScope.launch {
//            entriesCollection.snapshots().collect { snapshot ->
//                snapshot.documentChanges.forEach { change ->
//                    val entry = change.document.data<EntryFirestoreModel>().toDomain()
//                    when (change.type) {
//                        ChangeType.ADDED ->
//                            entryChanges.emit(FirestoreEvent.Added(entry))
//
//                        ChangeType.MODIFIED ->
//                            entryChanges.emit(FirestoreEvent.Modified(entry))
//
//                        ChangeType.REMOVED ->
//                            entryChanges.emit(FirestoreEvent.Removed(entry))
//
//                        else ->
//                            println("Unknown change type: ${change.type}")
//                    }
//                }
//            }
//        }
//    }


    // Referencia a la colección principal de diarios
    private val journalsCollection: CollectionReference
        get() = firestore.collection("journals")

    // Función de ayuda para obtener la subcolección de entradas
    private fun entriesCollection(journalId: String): CollectionReference =
        journalsCollection.document(journalId).collection("entries")

    override suspend fun getJournals(): List<FirebaseJournal> {
        return try {
            val snapshot = journalsCollection.get()
            // Mapea cada documento a FirebaseJournal, incluyendo el ID del documento
            snapshot.documents.map { doc ->
                doc.data<FirebaseJournal>().copy(id = doc.id)
            }
        } catch (e: Exception) {
            println("Error getting journals from Firestore: $e")
            emptyList() // O maneja el error de otra forma
        }
    }

    override suspend fun getEntries(journalId: String): List<FirebaseJournalEntry> {
        return try {
            val snapshot = entriesCollection(journalId).get()
            // Mapea cada documento a FirebaseJournalEntry, incluyendo el ID
            snapshot.documents.map { doc ->
                doc.data<FirebaseJournalEntry>().copy(id = doc.id)
            }
        } catch (e: Exception) {
            println("Error getting entries from Firestore for $journalId: $e")
            emptyList()
        }
    }

    override suspend fun saveJournal(journal: FirebaseJournal) {
        try {
            // Usa el ID del diario como ID del documento.
            // set() creará o sobrescribirá el documento.
            journalsCollection.document(journal.id).set(journal, encodeDefaults = true)
        } catch (e: Exception) {
            println("Error saving journal ${journal.id} to Firestore: $e")
            throw e // Relanza para que el repositorio lo maneje
        }
    }

    override suspend fun saveEntry(entry: FirebaseJournalEntry) {
        require(entry.journalId.isNotBlank()) { "Journal ID cannot be blank when saving an entry" }
        try {
            // Usa el ID de la entrada como ID del documento en la subcolección.
            entriesCollection(entry.journalId).document(entry.id).set(entry, encodeDefaults = true)
        } catch (e: Exception) {
            println("Error saving entry ${entry.id} to Firestore: $e")
            throw e
        }
    }

    override suspend fun deleteJournal(journalId: String) {
        try {
            // ¡Importante! Borrar un documento NO borra sus subcolecciones.
            // Debemos borrar las entradas primero.
            val entriesSnapshot = entriesCollection(journalId).get()
            if (entriesSnapshot.documents.isNotEmpty()) {
                // Usa un batch para borrar todas las entradas eficientemente
                firestore.batch().let {
                    entriesSnapshot.documents.forEach { doc ->
                        it.delete(doc.reference)
                    }
                }
            }

            // Ahora borra el documento del diario
            journalsCollection.document(journalId).delete()

        } catch (e: Exception) {
            println("Error deleting journal $journalId from Firestore: $e")
            throw e
        }
    }

    override suspend fun deleteEntry(journalId: String, entryId: String) {
        try {
            entriesCollection(journalId).document(entryId).delete()
        } catch (e: Exception) {
            println("Error deleting entry $entryId from Firestore: $e")
            throw e
        }
    }
}

@Serializable
data class FirebaseJournal(
    val id: String = "", // Firestore a menudo necesita un constructor sin args o valores por defecto
    val title: String = "",
    val description: String = "",
    val cover: String = "",
    // Las entradas se pueden manejar como subcolección o lista (aquí no la incluimos)
    // Es más escalable gestionarlas como una subcolección separada.
)

@Serializable
data class FirebaseJournalEntry(
    val id: String = "",
    val journalId: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "", // LocalDate como String (YYYY-MM-DD)
)
