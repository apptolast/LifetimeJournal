package com.apptolast.lifetimejournal.features.storybooks.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.lifetimejournal.data.repositories.StoryBookRepository
import com.apptolast.lifetimejournal.features.storybooks.data.StoryBookDetailState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class StoryBookDetailViewModel : ViewModel(), KoinComponent {

    private val storyBookRepository: StoryBookRepository by inject()

    private val _state = MutableStateFlow(StoryBookDetailState())
    val state = _state.asStateFlow()

    private val _navigationEvent = Channel<StoryBookDetailNavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun init(storyBookId: String) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        storyBookRepository.getStoryBookById(storyBookId)
            .catch { e ->
                _state.update {
                    it.copy(isLoading = false, error = e.message)
                }
            }
            .collect { storyBook ->
                _state.update {
                    it.copy(isLoading = false, storyBook = storyBook)
                }
            }
    }

    fun onEvent(event: StoryBookDetailEvent) {
        when (event) {
            is StoryBookDetailEvent.UpdateModificationPrompt -> {
                _state.update { it.copy(modificationPrompt = event.prompt) }
            }
            is StoryBookDetailEvent.RequestModification -> {
                requestModification()
            }
            is StoryBookDetailEvent.DeleteStoryBook -> {
                deleteStoryBook()
            }
        }
    }

    private fun requestModification() = viewModelScope.launch {
        val currentState = _state.value
        val storyBook = currentState.storyBook ?: return@launch
        val prompt = currentState.modificationPrompt.trim()

        if (prompt.isBlank()) return@launch

        _state.update { it.copy(isModifying = true, error = null) }

        val result = storyBookRepository.modifyStoryText(
            currentText = storyBook.generatedText,
            modificationPrompt = prompt,
        )

        result.fold(
            onSuccess = { modifiedText ->
                val updatedStoryBook = storyBook.copy(generatedText = modifiedText)
                storyBookRepository.updateStoryBook(updatedStoryBook)
                _state.update {
                    it.copy(
                        isModifying = false,
                        storyBook = updatedStoryBook,
                        modificationPrompt = "",
                    )
                }
            },
            onFailure = { error ->
                _state.update {
                    it.copy(
                        isModifying = false,
                        error = "Error modifying story: ${error.message}",
                    )
                }
            },
        )
    }

    private fun deleteStoryBook() = viewModelScope.launch {
        val storyBook = _state.value.storyBook ?: return@launch

        try {
            storyBookRepository.deleteStoryBook(storyBook)
            _navigationEvent.send(StoryBookDetailNavigationEvent.NavigateBack)
        } catch (e: Exception) {
            _state.update {
                it.copy(error = "Error deleting story book: ${e.message}")
            }
        }
    }
}

sealed interface StoryBookDetailEvent {
    data class UpdateModificationPrompt(val prompt: String) : StoryBookDetailEvent
    data object RequestModification : StoryBookDetailEvent
    data object DeleteStoryBook : StoryBookDetailEvent
}

sealed interface StoryBookDetailNavigationEvent {
    data object NavigateBack : StoryBookDetailNavigationEvent
}
