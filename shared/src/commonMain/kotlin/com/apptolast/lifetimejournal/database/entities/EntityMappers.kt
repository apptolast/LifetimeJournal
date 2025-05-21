package com.apptolast.lifetimejournal.database.entities

import com.apptolast.lifetimejournal.data.datamodel.Journal
import com.apptolast.lifetimejournal.data.datamodel.JournalEntry

///////////////////////////////////////////////////////////////////////////
// Domain to Entity
///////////////////////////////////////////////////////////////////////////
fun Journal.toEntity(): JournalEntity {
    return JournalEntity(
        id = this.id?.toInt() ?: 0,
        journalId = this.journalId,
        title = this.title,
        description = this.description,
        cover = this.cover,
        entryIds = this.entries.mapNotNull { it.id?.toInt() },
    )
}
fun JournalEntry.toEntity(journalId: String?): JournalEntryEntity {
    return JournalEntryEntity(
        id = this.id?.toInt() ?: 0,
        journalId = journalId,
        journalEntryId = this.journalEntryId,
        title = this.title,
        description = this.description,
        date = this.date,
    )
}

///////////////////////////////////////////////////////////////////////////
// Entity to Domain
///////////////////////////////////////////////////////////////////////////
fun JournalEntity.toDomain(entries: List<JournalEntry> = emptyList()): Journal {
    return Journal(
        id = this.id.toLong(),
        journalId = this.journalId,
        title = this.title,
        description = this.description,
        cover = this.cover,
        entries = entries.toMutableList(),
    )
}

fun JournalEntryEntity.toDomain(): JournalEntry {
    return JournalEntry(
        id = this.id.toLong(),
        journalId = this.journalId,
        journalEntryId = this.journalEntryId,
        title = this.title,
        description = this.description,
        date = this.date
    )
}

fun JournalWithEntriesEntity.toDomain(): Journal {
    return journal.toDomain(entries.map { it.toDomain() })
}

//fun JournalWithEntriesEntity.toDomain(): Journal {
//    return Journal(
//        id = this.journal.id.toLong(),
//        title = this.journal.title,
//        description = this.journal.description,
//        cover = this.journal.cover,
//        entries = this.entries.map { it.toDomain() }.toMutableList(),
//    )
//}
