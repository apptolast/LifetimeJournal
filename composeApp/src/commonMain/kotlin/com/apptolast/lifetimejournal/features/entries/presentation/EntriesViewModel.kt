package com.apptolast.lifetimejournal.features.entries.presentation

import androidx.lifecycle.ViewModel
import com.apptolast.lifetimejournal.domain.JournalEntry
import com.apptolast.lifetimejournal.features.entries.data.EntriesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent

class EntriesViewModel :
    ViewModel(),
    KoinComponent {

    private val _state = MutableStateFlow(EntriesState())
    val state = _state.asStateFlow()

    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.AddEntry -> {
                _state.update { currentState ->
                    currentState.copy(
                        entries = (
                            currentState.entries + JournalEntry(
                                title = event.title,
                                description = event.description,
                                date = event.date,
                            )
                            ) as MutableList<JournalEntry>,
                    )
                }
            }

            is UiEvent.SelectDate -> {
                _state.update { it.copy(selectedDate = event.date) }
            }

            is UiEvent.CalendarTitle -> {
                _state.update { it.copy(calendarTitle = event.value) }
            }
        }
    }
}

// /////////////////////////////////////////////////////////////////////////
// UI Events
// /////////////////////////////////////////////////////////////////////////
sealed interface UiEvent {
    data class AddEntry(val title: String, val description: String, val date: LocalDate) : UiEvent
    data class SelectDate(val date: LocalDate) : UiEvent
    data class CalendarTitle(val value: String) : UiEvent
}
