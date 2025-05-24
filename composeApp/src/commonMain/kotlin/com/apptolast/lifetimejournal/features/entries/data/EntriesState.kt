package com.apptolast.lifetimejournal.features.entries.data

import com.apptolast.lifetimejournal.data.datamodel.Journal
import com.kizitonwose.calendar.core.now
import kotlinx.datetime.LocalDate

data class EntriesState(
    val isLoading: Boolean = false,
    val journal: Journal? = null,
    val journalId: String? = null,
    val selectedDate: LocalDate = LocalDate.now(),
    val calendarTitle: String = "",
    val error: String? = null,
)
