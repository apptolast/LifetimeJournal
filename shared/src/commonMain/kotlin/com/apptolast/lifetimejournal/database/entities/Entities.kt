package com.apptolast.lifetimejournal.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.apptolast.lifetimejournal.COLUMN_ID
import com.apptolast.lifetimejournal.COLUMN_JOURNAL_ID
import com.apptolast.lifetimejournal.TABLE_JOURNAL
import com.apptolast.lifetimejournal.TABLE_JOURNAL_ENTRY
import com.apptolast.lifetimejournal.TABLE_STORY_BOOK
import kotlinx.datetime.LocalDate

@Entity(tableName = TABLE_JOURNAL)
data class JournalEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val cover: String
)

@Entity(
    tableName = TABLE_JOURNAL_ENTRY,
    foreignKeys = [ForeignKey(
        entity = JournalEntity::class,
        parentColumns = [COLUMN_ID],
        childColumns = [COLUMN_JOURNAL_ID],
        onDelete = ForeignKey.CASCADE // Si borras un diario, se borran sus entradas
    )],
    indices = [Index(value = [COLUMN_JOURNAL_ID])]
)
data class JournalEntryEntity(
    @PrimaryKey val id: String,
    val journalId: String,
    val title: String,
    val description: String,
    val date: LocalDate // Room usará el TypeConverter
)

@Entity(
    tableName = TABLE_STORY_BOOK,
    foreignKeys = [ForeignKey(
        entity = JournalEntity::class,
        parentColumns = [COLUMN_ID],
        childColumns = [COLUMN_JOURNAL_ID],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = [COLUMN_JOURNAL_ID])]
)
data class StoryBookEntity(
    @PrimaryKey val id: String,
    val journalId: String,
    val journalTitle: String,
    val title: String,
    val description: String,
    val coverUrl: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val storyStyle: String,
    val generatedText: String,
    val createdAt: LocalDate
)

data class JournalWithEntries(
    @Embedded val journal: JournalEntity,
    @Relation(
        parentColumn = COLUMN_ID,
        entityColumn = COLUMN_JOURNAL_ID
    )
    val entries: List<JournalEntryEntity>
)
