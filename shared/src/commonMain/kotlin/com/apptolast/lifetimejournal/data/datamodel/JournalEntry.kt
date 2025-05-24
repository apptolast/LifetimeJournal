package com.apptolast.lifetimejournal.data.datamodel

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class JournalEntry(
    val id: String , // ID único
    val journalId: String, // Clave foránea
    val title: String,
    val description: String,
    val date: LocalDate
)
