package com.apptolast.lifetimejournal.features.journals.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.lifetimejournal.data.repositories.AuthRepository
import com.apptolast.lifetimejournal.data.repositories.JournalRepository
import com.apptolast.lifetimejournal.features.journals.data.JournalsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class JournalsViewModel :
    ViewModel(),
    KoinComponent {

    private val authRepository: AuthRepository by inject()
    private val journalRepository: JournalRepository by inject()

    val user = authRepository.authState.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = null,
    )

    val journals = journalRepository.getAllJournals().stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList(),
    )

    private val _state = MutableStateFlow(JournalsState())
    val state = _state.asStateFlow()

//    fun deleteJournal(journalId: Long) = viewModelScope.launch {
//        _state.update { it.copy(isLoading = true) }
//        journalRepository.deleteJournal(journalId)
//        _state.update { it.copy(isLoading = false) }
//    }
}
