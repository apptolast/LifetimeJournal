package com.apptolast.lifetimejournal.features.createjournal.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.lifetimejournal.data.datamodel.Journal
import com.apptolast.lifetimejournal.data.repositories.JournalRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class CreateJournalViewModel :
    ViewModel(),
    KoinComponent {

    private val journalRepository: JournalRepository by inject()

    private val _state = MutableStateFlow(CreateJournalState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<CreateJournalUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: CreateJournalUiEvent) { // Renombrado a CreateJournalUiEvent para claridad
        when (event) {
            is CreateJournalUiEvent.OnTitleChange -> {
                _state.update { it.copy(title = event.value) }
            }

            is CreateJournalUiEvent.OnDescriptionChange -> {
                _state.update { it.copy(description = event.value) }
            }

            is CreateJournalUiEvent.OnCreateJournal -> {
                createJournal()
            }

            else -> {
                /* no-op */
            }
        }
    }

    private fun createJournal() = viewModelScope.launch {
        val currentTitle = _state.value.title.trim()
        val currentDescription = _state.value.description.trim()

        // Validación básica
        if (currentTitle.isBlank() || currentDescription.isBlank()) {
            // TODO: Enviar un evento de error a la UI (ej. _uiEvent.send(UiEvent.ShowError("Completa todos los campos")))
            println("Error: Título o descripción vacíos.")
            return@launch
        }

        _state.update { it.copy(isLoading = true) }

        try {
            // 1. Crear el objeto Journal CON un ID único
            val newJournal = Journal(
                id = Uuid.random().toString(), // ¡Usa tu generador de UUID KMP!
                title = currentTitle,
                description = currentDescription,
                cover = "https://loremflickr.com/230/300", // URL de placeholder
                entries = emptyList(), // Empieza sin entradas
            )

            // 2. Llamar al metodo del repositorio para añadir/guardar el diario
            //    Asumimos que tu repositorio tiene `addJournal` como lo diseñamos.
            journalRepository.addJournal(newJournal)

            // 3. Si todo fue bien, enviar el evento para navegar
            _uiEvent.send(CreateJournalUiEvent.NavigateToEntriesScreen(newJournal))

            // 4. (Opcional) Limpiar el estado si quieres que el usuario pueda crear otro diario
            _state.update { CreateJournalState() }
        } catch (e: Exception) {
            println("Error creating journal: ${e.message}")
            // TODO: Enviar un evento de error a la UI
            // _uiEvent.send(UiEvent.ShowError("Error al crear el diario: ${e.message}"))
        } finally {
            // 5. Asegurarse de que el estado de carga se desactive
            _state.update { it.copy(isLoading = false) }
        }
    }
}

// /////////////////////////////////////////////////////////////////////////
// UI Events
// /////////////////////////////////////////////////////////////////////////
sealed interface CreateJournalUiEvent {
    data class OnTitleChange(val value: String) : CreateJournalUiEvent
    data class OnDescriptionChange(val value: String) : CreateJournalUiEvent
    data object OnCreateJournal : CreateJournalUiEvent
    data class NavigateToEntriesScreen(val journal: Journal) : CreateJournalUiEvent
    // data class ShowError(val message: String) : CreateJournalUiEvent // Podrías añadir algo así
}

// /////////////////////////////////////////////////////////////////////////
// State
// /////////////////////////////////////////////////////////////////////////
data class CreateJournalState(val title: String = "", val description: String = "", val isLoading: Boolean = false)
