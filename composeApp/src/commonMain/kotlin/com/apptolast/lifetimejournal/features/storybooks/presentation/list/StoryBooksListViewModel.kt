package com.apptolast.lifetimejournal.features.storybooks.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.lifetimejournal.data.repositories.StoryBookRepository
import com.apptolast.lifetimejournal.features.storybooks.data.StoryBooksListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class StoryBooksListViewModel : ViewModel(), KoinComponent {

    private val storyBookRepository: StoryBookRepository by inject()

    private val _state = MutableStateFlow(StoryBooksListState())
    val state = _state.asStateFlow()

    init {
        loadStoryBooks()
    }

    private fun loadStoryBooks() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        storyBookRepository.getAllStoryBooks()
            .catch { e ->
                _state.update {
                    it.copy(isLoading = false, error = e.message)
                }
            }
            .collect { storyBooks ->
                _state.update {
                    it.copy(isLoading = false, storyBooks = storyBooks)
                }
            }
    }
}
