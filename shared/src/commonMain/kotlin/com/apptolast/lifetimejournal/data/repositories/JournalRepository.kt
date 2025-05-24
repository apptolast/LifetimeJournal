package com.apptolast.lifetimejournal.data.repositories

import com.apptolast.lifetimejournal.data.datamodel.Journal
import com.apptolast.lifetimejournal.data.datamodel.JournalEntry
import com.apptolast.lifetimejournal.database.dao.JournalDao
import com.apptolast.lifetimejournal.database.dao.JournalEntryDao
import com.apptolast.lifetimejournal.database.entities.toFirebaseJournal
import com.apptolast.lifetimejournal.database.entities.toFirebaseJournalEntry
import com.apptolast.lifetimejournal.database.entities.toJournal
import com.apptolast.lifetimejournal.database.entities.toJournalEntity
import com.apptolast.lifetimejournal.database.entities.toJournalEntry
import com.apptolast.lifetimejournal.database.entities.toJournalEntryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class JournalRepository(
    private val journalDao: JournalDao,
    private val journalEntryDao: JournalEntryDao,
    private val firebaseService: FirebaseService // Inyecta tu servicio Firebase
) {

    // --- Lectura ---

    fun getAllJournals(): Flow<List<Journal>> {
        // Devuelve datos de Room y opcionalmente refresca desde Firebase
        // Aquí, simplemente devolvemos el Flow de Room
        return journalDao.getAllJournalsWithEntries().map { list ->
            list.map { it.toJournal() }
        }
    }

    fun getJournalById(journalId: String): Flow<Journal?> {
        return journalDao.getJournalWithEntries(journalId).map { it?.toJournal() }
    }

    // --- Escritura (Ejemplo: Añadir Diario) ---

    suspend fun addJournal(journal: Journal) {
        try {
            // 1. Guardar en Firebase
            firebaseService.saveJournal(journal.toFirebaseJournal())
            journal.entries.forEach {
                firebaseService.saveEntry(it.toFirebaseJournalEntry())
            }

            // 2. Si Firebase tuvo éxito, guardar en Room
            journalDao.upsertJournalWithEntries(journal)

        } catch (e: Exception) {
            // Manejar errores (ej. guardar localmente para sincronizar después)
            println("Error adding journal: ${e.message}")
            throw e // O maneja el error de otra forma
        }
    }

    // --- Actualización ---

    suspend fun updateJournal(journal: Journal) {
        try {
            // 1. Actualizar en Firebase
            firebaseService.saveJournal(journal.toFirebaseJournal())
            // Aquí deberías manejar las entradas (añadir/borrar/actualizar)

            // 2. Actualizar en Room
            journalDao.upsertJournalWithEntries(journal)
        } catch (e: Exception) {
            println("Error updating journal: ${e.message}")
            throw e
        }
    }

    // --- Borrado ---

    suspend fun deleteJournal(journal: Journal) {
        try {
            // 1. Borrar de Firebase (necesitarás borrar las entradas también)
            firebaseService.deleteJournal(journal.id)
            // Lógica para borrar subcolección/entradas asociadas

            // 2. Borrar de Room (CASCADE se encargará de las entradas)
            journalDao.deleteJournal(journal.toJournalEntity())
        } catch (e: Exception) {
            println("Error deleting journal: ${e.message}")
            throw e
        }
    }

    // --- Operaciones con Entradas (Ejemplo: Añadir Entrada) ---

//    suspend fun addEntryToJournal(journalId: String, entry: JournalEntry) {
//        // Asegúrate de que la entrada tiene el journalId correcto
//        val entryToAdd = entry.copy(journalId = journalId)
//        try {
//            // 1. Guardar en Firebase
//            firebaseService.saveEntry(entryToAdd.toFirebaseJournalEntry())
//
//            // 2. Guardar en Room (Necesitarás un DAO para insertar solo una entrada)
//            // O actualizas el diario completo (más simple pero menos eficiente)
//            val currentJournal = journalDao.getJournalWithEntries(journalId) // Esto no es un Flow, necesitarías una versión suspend
//            // Mejor añadir un metodo al DAO:
//            // journalEntryDao.insertEntry(entryToAdd.toJournalEntryEntity())
//
//        } catch (e: Exception) {
//            println("Error adding entry: ${e.message}")
//            throw e
//        }
//    }

    // --- Sincronización (Opcional pero recomendable) ---

    suspend fun syncData() {
        try {
            val firebaseJournals = firebaseService.getJournals()
            firebaseJournals.forEach { fbJournal ->
                val fbEntries = firebaseService.getEntries(fbJournal.id)
                val journal = fbJournal.toJournal().copy(
                    entries = fbEntries.map { it.toJournalEntry() }
                )
                journalDao.upsertJournalWithEntries(journal)
            }
        } catch (e: Exception) {
            println("Error syncing data: ${e.message}")
        }
    }

    suspend fun updateEntry(entry: JournalEntry) {
        try {
            // 1. Actualizar en Firebase
            firebaseService.saveEntry(entry.toFirebaseJournalEntry())

            // 2. Actualizar en Room
            journalEntryDao.updateEntry(entry.toJournalEntryEntity())
        } catch (e: Exception) {
            println("Error updating entry: ${e.message}")
            throw e
        }
    }

    suspend fun deleteEntry(entry: JournalEntry) {
        try {
            // 1. Borrar de Firebase
            firebaseService.deleteEntry(entry.journalId, entry.id)

            // 2. Borrar de Room
            journalEntryDao.deleteEntry(entry.toJournalEntryEntity())
        } catch (e: Exception) {
            println("Error deleting entry: ${e.message}")
            throw e
        }
    }

    // Actualiza el metodo addEntryToJournal para que sea más eficiente:
    suspend fun addEntryToJournal(journalId: String, entry: JournalEntry) {
        // Asegúrate de que la entrada tenga el journalId correcto
        val entryToAdd = entry.copy(journalId = journalId)
        try {
            // 1. Guardar en Firebase
            firebaseService.saveEntry(entryToAdd.toFirebaseJournalEntry())

            // 2. Guardar en Room usando el DAO específico
            journalEntryDao.insertEntry(entryToAdd.toJournalEntryEntity())

        } catch (e: Exception) {
            println("Error adding entry: ${e.message}")
            throw e
        }
    }
}
