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
                viewModelScope.launch {
                    Journal(
                        title = _state.value.title,
                        description = _state.value.description,
                        cover = "https://fastly.picsum.photos/id/237/200/300.jpg?hmac=TmmQSbShHz9CdQm0NkEjx1Dyh_Y984R9LpNrpvH2D_U",
                    ).let { journal ->
                        journalRepository.createJournal(journal)
                        _uiEvent.send(UiEvent.NavigateToEntriesScreen(journal))
                    }
                }
            }

            else -> {/* no-op */
            }
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
