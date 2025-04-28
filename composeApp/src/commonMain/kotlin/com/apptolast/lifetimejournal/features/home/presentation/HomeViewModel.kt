package com.apptolast.lifetimejournal.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.lifetimejournal.features.home.data.HomeState
import com.apptolast.lifetimejournal.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeViewModel :
    ViewModel(),
    KoinComponent {

    private val authRepository: AuthRepository by inject()

    val user = authRepository.authState.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = null,
    )

    private val _state = MutableStateFlow<HomeState>(HomeState())
    val state = _state.asStateFlow()
}
