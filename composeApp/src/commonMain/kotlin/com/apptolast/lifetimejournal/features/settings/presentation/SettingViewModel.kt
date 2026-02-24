package com.apptolast.lifetimejournal.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.lifetimejournal.data.repositories.AuthRepository
import com.apptolast.lifetimejournal.features.settings.data.SettingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingViewModel :
    ViewModel(),
    KoinComponent {

    val authRepository: AuthRepository by inject()

    private val _state = MutableStateFlow(SettingState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.authState.collect { user ->
                _state.update { it.copy(user = user) }
            }
        }
    }

    fun signOut() = viewModelScope.launch {
        authRepository.signOut()
    }

    fun deleteAccount() = viewModelScope.launch {
        // TODO: Implement account deletion
        authRepository.signOut()
    }
}
