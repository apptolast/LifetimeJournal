package com.apptolast.lifetimejournal.data.datamodel

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class StoryBook(
    val id: String,
    val journalId: String,
    val journalTitle: String,
    val title: String,
    val description: String,
    val coverUrl: String = "",
    val startDate: LocalDate,
    val endDate: LocalDate,
    val storyStyle: String,
    val generatedText: String,
    val createdAt: LocalDate,
)
