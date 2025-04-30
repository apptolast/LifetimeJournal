package com.apptolast.lifetimejournal.features.createjournal.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.lifetimejournal.features.createjournal.data.CreateJournalState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class CreateJournalViewModel :
    ViewModel(),
    KoinComponent {

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

            UiEvent.OnCreateJournal -> {
                viewModelScope.launch {
                    createJournal()
                    _uiEvent.send(UiEvent.OnCreateJournal)
                }
            }
        }
    }

    suspend fun createJournal() {
        // TODO: Create Journal and store it properly
    }
}

// /////////////////////////////////////////////////////////////////////////
// UI Events
// /////////////////////////////////////////////////////////////////////////
sealed interface UiEvent {
    data class OnTitleChange(val value: String) : UiEvent
    data class OnDescriptionChange(val value: String) : UiEvent
    data object OnCreateJournal : UiEvent
}
