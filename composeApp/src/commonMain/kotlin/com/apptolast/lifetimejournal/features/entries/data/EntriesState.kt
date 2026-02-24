package com.apptolast.lifetimejournal.features.entries.data

import com.apptolast.lifetimejournal.data.datamodel.Journal
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
data class EntriesState(
    val isLoading: Boolean = false,
    val journal: Journal? = null,
    val journalId: String? = null,
    val selectedDate: LocalDate = Clock.System.todayIn(currentSystemDefault()),
    val calendarTitle: String = "",
    val error: String? = null,
)
