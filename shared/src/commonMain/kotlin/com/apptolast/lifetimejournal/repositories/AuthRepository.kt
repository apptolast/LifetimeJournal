package com.apptolast.lifetimejournal.repositories

import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
) : AuthRepository {

    private val _authState = MutableStateFlow(false)
    override val authState: StateFlow<Boolean> = _authState.asStateFlow()

    override fun loginWithGoogle() {
        return _authState.update{true}
    }
}

interface AuthRepository {
    val authState :StateFlow<Boolean>

    fun loginWithGoogle()
}
