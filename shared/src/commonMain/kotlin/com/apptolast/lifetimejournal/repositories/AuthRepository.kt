package com.apptolast.lifetimejournal.repositories

import com.sunildhiman90.kmauth.core.KMAuthUser
import com.sunildhiman90.kmauth.google.GoogleAuthManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthRepositoryImpl(private val googleAuthManager: GoogleAuthManager) : AuthRepository {

    private val _authState: MutableStateFlow<KMAuthUser?> = MutableStateFlow(null)
    override val authState: StateFlow<KMAuthUser?> = _authState.asStateFlow()

    override suspend fun loginWithGoogle() {
        googleAuthManager.signIn { user, error ->
            if (error != null) {
                println("Error in google Sign In: ${error.message}")
            }
            println("User: $user")
            _authState.update { user }
        }
    }
}

interface AuthRepository {
    val authState: StateFlow<KMAuthUser?>

    suspend fun loginWithGoogle()
}
