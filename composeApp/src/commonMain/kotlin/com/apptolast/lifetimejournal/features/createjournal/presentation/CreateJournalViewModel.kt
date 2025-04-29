package com.apptolast.lifetimejournal.features.createjournal.presentation

import androidx.lifecycle.ViewModel
import com.apptolast.lifetimejournal.features.createjournal.data.CreateJournalState
import com.apptolast.lifetimejournal.features.createjournal.data.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.component.KoinComponent

class CreateJournalViewModel :
    ViewModel(),
    KoinComponent {

    private val _state = MutableStateFlow(CreateJournalState())
    val state = _state.asStateFlow()

    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.OnTitleChange -> {
                println("Title: ${event.value}")
                _state.update { it.copy(title = event.value) }
            }

            is UiEvent.OnDescriptionChange -> {
                println("Description: ${event.value}")
                _state.update { it.copy(description = event.value) }
            }
        }
    }
}
