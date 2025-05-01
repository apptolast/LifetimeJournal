package com.apptolast.lifetimejournal.domain

data class Book(val title: String, val description: String, val cover: String, val entries: List<JournalEntry>)
