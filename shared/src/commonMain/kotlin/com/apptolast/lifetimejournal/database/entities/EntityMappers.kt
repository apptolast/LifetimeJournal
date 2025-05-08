package com.apptolast.lifetimejournal.database.mappers

import com.apptolast.lifetimejournal.data.datamodel.Journal
import com.apptolast.lifetimejournal.data.datamodel.JournalEntry
import com.apptolast.lifetimejournal.database.entities.JournalEntity
import com.apptolast.lifetimejournal.database.entities.JournalEntryEntity
import com.apptolast.lifetimejournal.database.entities.JournalWithEntriesEntity

/**
 * Extension functions to map between domain models and database entities
 */

// Journal Entry Mappers
fun JournalEntry.toEntity(journalId: Int): JournalEntryEntity {
    return JournalEntryEntity(
        id = this.id?.toInt() ?: 0,
        title = this.title,
        description = this.description,
        date = this.date,
        journalId = journalId,
    )
}

fun JournalEntryEntity.toDomain(): JournalEntry {
    return JournalEntry(
        id = this.id.toLong(),
        title = this.title,
        description = this.description,
        date = this.date,
    )
}

// Journal Mappers
fun Journal.toEntity(): JournalEntity {
    return JournalEntity(
//        id = this.id?.toInt() ?: -1,
        title = this.title,
        description = this.description,
        cover = this.cover,
        entryIds = this.entries.mapNotNull { it.id?.toInt() },
    )
}

fun JournalWithEntriesEntity.toDomain(): Journal {
    return Journal(
        id = this.journal.id.toLong(),
        title = this.journal.title,
        description = this.journal.description,
        cover = this.journal.cover,
        entries = this.entries.map { it.toDomain() }.toMutableList(),
    )
}

fun JournalEntity.toDomain(entries: List<JournalEntry> = emptyList()): Journal {
    return Journal(
        id = this.id.toLong(),
        title = this.title,
        description = this.description,
        cover = this.cover,
        entries = entries.toMutableList(),
    )
}
