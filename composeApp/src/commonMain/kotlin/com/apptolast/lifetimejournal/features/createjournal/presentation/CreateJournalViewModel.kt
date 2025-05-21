package com.apptolast.lifetimejournal.features.createjournal.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.lifetimejournal.data.datamodel.Journal
import com.apptolast.lifetimejournal.data.repositories.JournalRepository
import com.apptolast.lifetimejournal.features.createjournal.data.CreateJournalState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CreateJournalViewModel :
    ViewModel(),
    KoinComponent {

    private val journalRepository: JournalRepository by inject()

    private val _state = MutableStateFlow(CreateJournalState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.OnTitleChange -> {
                _state.update { it.copy(title = event.value) }
            }

            is UiEvent.OnDescriptionChange -> {
                _state.update { it.copy(description = event.value) }
            }

            is UiEvent.OnCreateJournal -> {
                createJournal()
            }


            else -> {
                /* no-op */
            }
        }
    }

    private fun createJournal() = viewModelScope.launch {
        try {
            _state.update { it.copy(isLoading = true) }

            val journal = Journal(
                title = _state.value.title,
                description = _state.value.description,
                cover = "https://loremflickr.com/230/300",
                // We don't set firestoreId, the repository will handle this
            )

            // This operation now saves the journal to both Room and Firestore
            val journalId = journalRepository.createJournal(journal)

            // Get the fully created journal with its IDs
            val createdJournal = journalRepository.getJournal(journalId)

            if (createdJournal != null) {
                _uiEvent.send(UiEvent.NavigateToEntriesScreen(createdJournal))
            } else {
                println("Failed to retrieve the created journal")
            }
        } catch (e: Exception) {
            println("Error creating journal: ${e.message}")
        } finally {
            _state.update { it.copy(isLoading = false) }
        }
    }

}

// /////////////////////////////////////////////////////////////////////////
// UI Events
// /////////////////////////////////////////////////////////////////////////
sealed interface UiEvent {
    data class OnTitleChange(val value: String) : UiEvent
    data class OnDescriptionChange(val value: String) : UiEvent
    data object OnCreateJournal : UiEvent
    data class NavigateToEntriesScreen(val journal: Journal) : UiEvent
}
