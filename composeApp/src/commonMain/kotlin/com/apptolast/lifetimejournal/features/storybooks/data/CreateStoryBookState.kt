package com.apptolast.lifetimejournal.features.storybooks.data

import com.apptolast.lifetimejournal.data.datamodel.Journal
import kotlinx.datetime.LocalDate

data class CreateStoryBookState(
    val isLoading: Boolean = false,
    val journals: List<Journal> = emptyList(),
    val selectedJournal: Journal? = null,
    val startDateMillis: Long? = null,
    val endDateMillis: Long? = null,
    val storyStyle: String = "",
    val error: String? = null,
    val isGenerating: Boolean = false,
)
