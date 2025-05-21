package com.apptolast.lifetimejournal.data.datamodel

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class JournalEntry(
    val id: Long? = null,
    val journalId: String? = null,
    val journalEntryId: String? = null,
    val title: String,
    val description: String,
    val date: LocalDate,
)
