package com.apptolast.lifetimejournal.domain

data class Journal(
    val id: String,
    val title: String,
    val description: String,
    val cover: String,
    val entries: List<JournalEntry>,
)
