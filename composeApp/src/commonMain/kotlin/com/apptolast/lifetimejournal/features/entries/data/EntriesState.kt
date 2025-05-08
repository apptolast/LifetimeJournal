package com.apptolast.lifetimejournal.features.entries.data

import com.apptolast.lifetimejournal.data.datamodel.Journal
import com.kizitonwose.calendar.core.now
import kotlinx.datetime.LocalDate

data class EntriesState(
    val isLoading: Boolean = false,
    val calendarTitle: String = "${LocalDate.now().month.name} ${LocalDate.now().year}",
    val selectedDate: LocalDate = LocalDate.now(),
    val journal: Journal? = null,
//    val entries: MutableList<JournalEntry> = mutableListOf(),
)
