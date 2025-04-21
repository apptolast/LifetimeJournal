package com.apptolast.lifetimejournal.repositories

import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
) : AuthRepository {

    private val _authState = MutableStateFlow(false)
    override val authState: StateFlow<Boolean> = _authState.asStateFlow()

    override fun loginWithGoogle() {
        TODO()
    }
}

interface AuthRepository {
    val authState :StateFlow<Boolean>

    fun loginWithGoogle()
}
