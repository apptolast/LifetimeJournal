package com.apptolast.lifetimejournal.features.entries.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.lifetimejournal.data.datamodel.JournalEntry
import com.apptolast.lifetimejournal.data.repositories.JournalRepository
import com.apptolast.lifetimejournal.features.entries.data.EntriesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EntriesViewModel :
    ViewModel(),
    KoinComponent {

    private val journalRepository: JournalRepository by inject()

    private val _state = MutableStateFlow(EntriesState())
    val state = _state.asStateFlow()

    fun init(journalId: Long?) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        journalRepository.getJournal(journalId)?.let { journal ->
            _state.update {
                it.copy(
                    journal = journal,
                    isLoading = false,
                )
            }
        } ?: run {
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.AddEntry -> {
                viewModelScope.launch {
//                    val journalId = _state.value.journal?.id // Seguro que tenemos el journal.id en este punto?
//                    if (journalId != null) {
                        val entry = JournalEntry(
                            title = event.title,
                            description = event.description,
                            date = event.date,
                        )

                        // Crear la entrada y obtener su ID
                        journalRepository.addEntryToJournal(/*journalId,*/ entry)

                        // Recargar el journal completo para obtener la lista actualizada de entradas
                        journalRepository.getJournal(journalId)?.let { updatedJournal ->
                            _state.update {
                                it.copy(journal = updatedJournal)
                            }
                        }
                    }
//                }
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
