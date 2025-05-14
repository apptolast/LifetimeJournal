package com.apptolast.lifetimejournal.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.lifetimejournal.data.repositories.AuthRepository
import com.apptolast.lifetimejournal.features.settings.data.SettingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingViewModel :
    ViewModel(),
    KoinComponent {

    val authRepository: AuthRepository by inject()

    private val _state = MutableStateFlow<SettingState>(SettingState())
    val state = _state.asStateFlow()

    fun signOut() = viewModelScope.launch {
        authRepository.signOut()
    }
}
