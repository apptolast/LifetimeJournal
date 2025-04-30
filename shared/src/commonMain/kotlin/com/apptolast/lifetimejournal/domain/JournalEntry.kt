package com.apptolast.lifetimejournal.domain

import kotlinx.datetime.LocalDate

data class JournalEntry(
    val title: String,
    val description: String,
    val date: LocalDate,
)
