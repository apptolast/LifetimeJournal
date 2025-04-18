package com.apptolast.lifetimejournal.features.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptolast.lifetimejournal.features.login.data.LoginState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class LoginViewModel : ViewModel(), KoinComponent {


    private val _state = MutableStateFlow<LoginState>(LoginState())
    val state = _state.asStateFlow()

    init{
        println("Current user: ${Firebase.auth.currentUser}")
        loginWithGoogle()
    }

    fun loginWithGoogle() = viewModelScope.launch {
        val a = Firebase.auth.signInWithEmailAndPassword("", "")
    }
}
