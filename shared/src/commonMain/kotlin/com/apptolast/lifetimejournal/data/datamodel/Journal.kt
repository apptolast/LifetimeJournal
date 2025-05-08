package com.apptolast.lifetimejournal.data.datamodel

import kotlinx.serialization.Serializable

@Serializable
data class Journal(
    val id: Long? = null,
    val title: String,
    val description: String,
    val cover: String,
    val entries: MutableList<JournalEntry> = mutableListOf(),
)
