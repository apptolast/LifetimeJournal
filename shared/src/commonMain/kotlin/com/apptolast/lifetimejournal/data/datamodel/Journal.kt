package com.apptolast.lifetimejournal.data.datamodel

import kotlinx.serialization.Serializable

@Serializable
data class Journal(
    val id: String, // ID único
    val title: String,
    val description: String,
    val cover: String,
    val entries: List<JournalEntry> = emptyList()
)
