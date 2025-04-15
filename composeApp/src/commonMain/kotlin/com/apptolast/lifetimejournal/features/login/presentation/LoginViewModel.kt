package com.apptolast.lifetimejournal.features.login.presentation

import androidx.lifecycle.ViewModel
import com.apptolast.lifetimejournal.features.login.data.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent

class LoginViewModel :
    ViewModel(),
    KoinComponent {

    private val _state = MutableStateFlow<LoginState>(LoginState())
    val state = _state.asStateFlow()
}
