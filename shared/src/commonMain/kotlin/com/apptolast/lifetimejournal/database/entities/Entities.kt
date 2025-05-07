package com.apptolast.lifetimejournal.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.apptolast.lifetimejournal.TABLE_JOURNAL
import com.apptolast.lifetimejournal.TABLE_JOURNAL_ENTRY
import kotlinx.datetime.LocalDate

/**
 * Journal Entry Entity for Room
 */
@Entity(tableName = TABLE_JOURNAL_ENTRY)
data class JournalEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val date: LocalDate,

    // Foreign key to link entries to journals
    @ColumnInfo(name = "journal_id")
    val journalId: Int,
)

/**
 * Journal Entity for Room
 */
@Entity(tableName = TABLE_JOURNAL)
data class JournalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val cover: String,

    // Store entry IDs as a separate field (managed by Converters)
    @ColumnInfo(name = "entry_ids")
    val entryIds: List<Int> = emptyList(),
)

/**
 * Relationship class to handle the one-to-many relationship
 * between Journal and its Entries
 */
data class JournalWithEntriesEntity(
    @Embedded val journal: JournalEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "journal_id",
    )
    val entries: List<JournalEntryEntity> = emptyList(),
)
