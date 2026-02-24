package com.apptolast.lifetimejournal.database.entities

import com.apptolast.lifetimejournal.data.datamodel.Journal
import com.apptolast.lifetimejournal.data.datamodel.JournalEntry
import com.apptolast.lifetimejournal.data.datamodel.StoryBook
import com.apptolast.lifetimejournal.data.repositories.FirebaseJournal
import com.apptolast.lifetimejournal.data.repositories.FirebaseJournalEntry
import kotlinx.datetime.LocalDate

// Journal Mappers
fun JournalEntity.toJournal(): Journal {
    return Journal(id, title, description, cover, emptyList()) // Las entradas se cargan por separado
}

fun Journal.toJournalEntity(): JournalEntity {
    return JournalEntity(id, title, description, cover)
}

fun JournalWithEntries.toJournal(): Journal {
    return Journal(
        id = journal.id,
        title = journal.title,
        description = journal.description,
        cover = journal.cover,
        entries = entries.map { it.toJournalEntry() }
    )
}

fun FirebaseJournal.toJournal(): Journal {
    return Journal(id, title, description, cover, emptyList())
}

fun Journal.toFirebaseJournal(): FirebaseJournal {
    return FirebaseJournal(id, title, description, cover)
}

// JournalEntry Mappers
fun JournalEntryEntity.toJournalEntry(): JournalEntry {
    return JournalEntry(id, journalId, title, description, date)
}

fun JournalEntry.toJournalEntryEntity(): JournalEntryEntity {
    return JournalEntryEntity(id, journalId, title, description, date)
}

fun FirebaseJournalEntry.toJournalEntry(): JournalEntry {
    return JournalEntry(id, journalId, title, description, LocalDate.parse(date))
}

fun JournalEntry.toFirebaseJournalEntry(): FirebaseJournalEntry {
    return FirebaseJournalEntry(id, journalId, title, description, date.toString())
}

// StoryBook Mappers
fun StoryBookEntity.toStoryBook(): StoryBook {
    return StoryBook(
        id = id,
        journalId = journalId,
        journalTitle = journalTitle,
        title = title,
        description = description,
        coverUrl = coverUrl,
        startDate = startDate,
        endDate = endDate,
        storyStyle = storyStyle,
        generatedText = generatedText,
        createdAt = createdAt
    )
}

fun StoryBook.toStoryBookEntity(): StoryBookEntity {
    return StoryBookEntity(
        id = id,
        journalId = journalId,
        journalTitle = journalTitle,
        title = title,
        description = description,
        coverUrl = coverUrl,
        startDate = startDate,
        endDate = endDate,
        storyStyle = storyStyle,
        generatedText = generatedText,
        createdAt = createdAt
    )
}
