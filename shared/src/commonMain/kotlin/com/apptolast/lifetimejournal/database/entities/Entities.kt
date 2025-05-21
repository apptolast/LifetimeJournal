package com.apptolast.lifetimejournal.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.apptolast.lifetimejournal.COLUMN_NAME_JOURNAL_ENTRY_ID
import com.apptolast.lifetimejournal.COLUMN_NAME_JOURNAL_ID
import com.apptolast.lifetimejournal.TABLE_JOURNAL
import com.apptolast.lifetimejournal.TABLE_JOURNAL_ENTRY
import kotlinx.datetime.LocalDate

/**
 * Journal Entity for Room
 */
@Entity(tableName = TABLE_JOURNAL)
data class JournalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = COLUMN_NAME_JOURNAL_ID) val journalId: String? = null,
    val title: String,
    val description: String,
    val cover: String,
    // Store entry IDs as a separate field (managed by Converters)
    @ColumnInfo(name = "entry_ids") val entryIds: List<Int> = emptyList(),
)

/**
 * Journal Entry Entity for Room
 */
@Entity(tableName = TABLE_JOURNAL_ENTRY)
data class JournalEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    // Foreign key to link entries to journals
    @ColumnInfo(name = COLUMN_NAME_JOURNAL_ID) val journalId: String? = null,
    @ColumnInfo(name = COLUMN_NAME_JOURNAL_ENTRY_ID) val journalEntryId: String? = null,
//    @ColumnInfo(name = COLUMN_NAME_JOURNAL_ID) val journalIdReference: String? = null,
    val title: String,
    val description: String,
    val date: LocalDate,
)


/**
 * Relationship class to handle the one-to-many relationship
 * between Journal and its Entries
 */
data class JournalWithEntriesEntity(
    @Embedded val journal: JournalEntity,

    @Relation(
        parentColumn = COLUMN_NAME_JOURNAL_ID,
        entityColumn = COLUMN_NAME_JOURNAL_ENTRY_ID,
    )
    val entries: List<JournalEntryEntity> = emptyList(),
)
