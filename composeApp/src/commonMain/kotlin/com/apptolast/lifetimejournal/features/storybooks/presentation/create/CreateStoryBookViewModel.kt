package com.apptolast.lifetimejournal.features.storybooks.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.lifetimejournal.data.datamodel.Journal
import com.apptolast.lifetimejournal.data.datamodel.StoryBook
import com.apptolast.lifetimejournal.data.repositories.JournalRepository
import com.apptolast.lifetimejournal.data.repositories.StoryBookRepository
import com.apptolast.lifetimejournal.features.storybooks.data.CreateStoryBookState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class CreateStoryBookViewModel : ViewModel(), KoinComponent {

    private val journalRepository: JournalRepository by inject()
    private val storyBookRepository: StoryBookRepository by inject()

    private val _state = MutableStateFlow(CreateStoryBookState())
    val state = _state.asStateFlow()

    private val _navigationEvent = Channel<CreateStoryBookNavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    init {
        loadJournals()
    }

    private fun loadJournals() = viewModelScope.launch {
        journalRepository.getAllJournals()
            .catch { e ->
                _state.update { it.copy(error = e.message) }
            }
            .collect { journals ->
                _state.update {
                    it.copy(
                        journals = journals,
                        selectedJournal = journals.firstOrNull(),
                    )
                }
            }
    }

    fun onEvent(event: CreateStoryBookEvent) {
        when (event) {
            is CreateStoryBookEvent.SelectJournal -> {
                _state.update { it.copy(selectedJournal = event.journal) }
            }
            is CreateStoryBookEvent.SelectDateRange -> {
                _state.update {
                    it.copy(
                        startDateMillis = event.startMillis,
                        endDateMillis = event.endMillis,
                    )
                }
            }
            is CreateStoryBookEvent.UpdateStoryStyle -> {
                _state.update { it.copy(storyStyle = event.style) }
            }
            is CreateStoryBookEvent.GenerateBook -> {
                generateStoryBook()
            }
        }
    }

    private fun generateStoryBook() = viewModelScope.launch {
        val currentState = _state.value
        val journal = currentState.selectedJournal ?: return@launch
        val startMillis = currentState.startDateMillis ?: return@launch
        val endMillis = currentState.endDateMillis ?: return@launch

        _state.update { it.copy(isGenerating = true, error = null) }

        try {
            val startDate = millisToLocalDate(startMillis)
            val endDate = millisToLocalDate(endMillis)

            // Get journal with entries
            val journalWithEntries = journalRepository.getJournalById(journal.id).first()
                ?: throw Exception("Journal not found")

            // Filter entries by date range
            val filteredEntries = journalWithEntries.entries.filter { entry ->
                entry.date >= startDate && entry.date <= endDate
            }

            if (filteredEntries.isEmpty()) {
                _state.update {
                    it.copy(
                        isGenerating = false,
                        error = "No entries found in the selected date range",
                    )
                }
                return@launch
            }

            // Generate story using AI
            val storyResult = storyBookRepository.generateStoryText(
                entries = filteredEntries,
                storyStyle = currentState.storyStyle.ifBlank { "a heartwarming family story" },
            )

            storyResult.fold(
                onSuccess = { generatedText ->
                    val storyBook = StoryBook(
                        id = Uuid.random().toString(),
                        journalId = journal.id,
                        journalTitle = journal.title,
                        title = "Our ${journal.title} Story",
                        description = "A collection of memories from our family trip.",
                        coverUrl = "",
                        startDate = startDate,
                        endDate = endDate,
                        storyStyle = currentState.storyStyle,
                        generatedText = generatedText,
                        createdAt = startDate, // Use start date as creation date for simplicity
                    )

                    storyBookRepository.createStoryBook(storyBook)
                    _state.update { it.copy(isGenerating = false) }
                    _navigationEvent.send(CreateStoryBookNavigationEvent.NavigateToDetail(storyBook.id))
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isGenerating = false,
                            error = "Error generating story: ${error.message}",
                        )
                    }
                },
            )
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isGenerating = false,
                    error = "Error: ${e.message}",
                )
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun millisToLocalDate(millis: Long): LocalDate {
        // Convert millis to days since epoch and create LocalDate
        val days = (millis / (24 * 60 * 60 * 1000)).toInt()
        return LocalDate.fromEpochDays(days)
    }
}

sealed interface CreateStoryBookEvent {
    data class SelectJournal(val journal: Journal) : CreateStoryBookEvent
    data class SelectDateRange(val startMillis: Long?, val endMillis: Long?) : CreateStoryBookEvent
    data class UpdateStoryStyle(val style: String) : CreateStoryBookEvent
    data object GenerateBook : CreateStoryBookEvent
}

sealed interface CreateStoryBookNavigationEvent {
    data class NavigateToDetail(val storyBookId: String) : CreateStoryBookNavigationEvent
}
