package com.apptolast.lifetimejournal.features.entries.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.lifetimejournal.data.datamodel.JournalEntry
import com.apptolast.lifetimejournal.data.repositories.JournalRepository
import com.apptolast.lifetimejournal.features.entries.data.EntriesState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class EntriesViewModel :
    ViewModel(),
    KoinComponent {

    private val journalRepository: JournalRepository by inject()

    private val _state = MutableStateFlow(EntriesState())
    val state = _state.asStateFlow()

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun init(journalId: String?) = viewModelScope.launch {
        if (journalId == null) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = "Journal ID is required",
                )
            }
            return@launch
        }

        _state.update { it.copy(isLoading = true, journalId = journalId) }

        try {
            journalRepository.getJournalById(journalId)
                .catch { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Error loading journal: ${exception.message}",
                        )
                    }
                }
                .collect { journal ->
                    _state.update {
                        it.copy(
                            journal = journal,
                            isLoading = false,
                            error = null,
                        )
                    }
                }
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = "Unexpected error: ${e.message}",
                )
            }
        }
    }

    fun onEvent(event: UiEvent) = viewModelScope.launch {
        when (event) {
            is UiEvent.AddEntry -> {
                addEntry(event.title, event.description, event.date)
            }

            is UiEvent.SelectDate -> {
                _state.update { it.copy(selectedDate = event.date) }
            }

            is UiEvent.CalendarTitle -> {
                _state.update { it.copy(calendarTitle = event.value) }
            }

            is UiEvent.UpdateEntry -> {
                updateEntry(event.entry)
            }

            is UiEvent.DeleteEntry -> {
                deleteEntry(event.entry)
            }

            is UiEvent.UpdateJournal -> {
                updateJournal(event.title, event.description)
            }

            is UiEvent.DeleteJournal -> {
                deleteJournal()
            }

            is UiEvent.ClearError -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    private suspend fun addEntry(title: String, description: String, date: LocalDate) {
        val currentState = _state.value
        val journalId = currentState.journalId

        if (journalId == null) {
            _state.update { it.copy(error = "No journal selected") }
            return
        }

        if (title.isBlank() || description.isBlank()) {
            _state.update { it.copy(error = "Title and description cannot be empty") }
            return
        }

        _state.update { it.copy(isLoading = true) }

        try {
            val entry = JournalEntry(
                id = Uuid.random().toString(),
                journalId = journalId,
                title = title.trim(),
                description = description.trim(),
                date = date,
            )

            journalRepository.addEntryToJournal(journalId, entry)

            _state.update {
                it.copy(
                    isLoading = false,
                    error = null,
                )
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = "Error adding entry: ${e.message}",
                )
            }
        }
    }

    private suspend fun updateEntry(entry: JournalEntry) {
        _state.update { it.copy(isLoading = true) }

        try {
            journalRepository.updateEntry(entry)

            _state.update {
                it.copy(
                    isLoading = false,
                    error = null,
                )
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = "Error updating entry: ${e.message}",
                )
            }
        }
    }

    private suspend fun deleteEntry(entry: JournalEntry) {
        _state.update { it.copy(isLoading = true) }

        try {
            journalRepository.deleteEntry(entry)

            _state.update {
                it.copy(
                    isLoading = false,
                    error = null,
                )
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = "Error deleting entry: ${e.message}",
                )
            }
        }
    }

    private suspend fun updateJournal(title: String, description: String) {
        val currentJournal = _state.value.journal ?: return

        if (title.isBlank()) {
            _state.update { it.copy(error = "Title cannot be empty") }
            return
        }

        _state.update { it.copy(isLoading = true) }

        try {
            val updatedJournal = currentJournal.copy(
                title = title.trim(),
                description = description.trim(),
            )
            journalRepository.updateJournal(updatedJournal)

            _state.update {
                it.copy(
                    isLoading = false,
                    error = null,
                )
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = "Error updating journal: ${e.message}",
                )
            }
        }
    }

    private suspend fun deleteJournal() {
        val currentJournal = _state.value.journal ?: return

        _state.update { it.copy(isLoading = true) }

        try {
            journalRepository.deleteJournal(currentJournal)
            _navigationEvent.send(NavigationEvent.NavigateBack)
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = "Error deleting journal: ${e.message}",
                )
            }
        }
    }
}

// /////////////////////////////////////////////////////////////////////////
// Navigation Events
// /////////////////////////////////////////////////////////////////////////
sealed interface NavigationEvent {
    data object NavigateBack : NavigationEvent
}

// /////////////////////////////////////////////////////////////////////////
// UI Events
// /////////////////////////////////////////////////////////////////////////
sealed interface UiEvent {
    data class AddEntry(val title: String, val description: String, val date: LocalDate) : UiEvent
    data class UpdateEntry(val entry: JournalEntry) : UiEvent
    data class DeleteEntry(val entry: JournalEntry) : UiEvent
    data class SelectDate(val date: LocalDate) : UiEvent
    data class CalendarTitle(val value: String) : UiEvent
    data class UpdateJournal(val title: String, val description: String) : UiEvent
    data object DeleteJournal : UiEvent
    data object ClearError : UiEvent
}
