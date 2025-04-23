package com.apptolast.lifetimejournal.repositories

import com.sunildhiman90.kmauth.google.GoogleAuthManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthRepositoryImpl(
    private val googleAuthManager: GoogleAuthManager,
) : AuthRepository {

    private val _authState = MutableStateFlow(false)
    override val authState: StateFlow<Boolean> = _authState.asStateFlow()

    override suspend fun loginWithGoogle() {
        googleAuthManager.signIn { user, error ->
            if (error != null) {
                println("Error in google Sign In: ${error.message}")
            }
            if (user != null) {
                println("Login Successful user: $user")
                _authState.update { true }
            }
        }
    }
}

interface AuthRepository {
    val authState: StateFlow<Boolean>

    suspend fun loginWithGoogle()
}
