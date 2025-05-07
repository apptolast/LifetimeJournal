package com.apptolast.lifetimejournal.features.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.lifetimejournal.features.login.data.LoginState
import com.apptolast.lifetimejournal.data.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel :
    ViewModel(),
    KoinComponent {
    private val authRepository: AuthRepository by inject()

    private val _state = MutableStateFlow<LoginState>(LoginState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.authState.collect { authState ->
                _state.update { it.copy(isLoading = false, isAuthenticated = authState != null) }
            }
        }
        loginWithGoogle()
    }

    fun loginWithGoogle() = viewModelScope.launch {
        authRepository.loginWithGoogle()
    }
}
