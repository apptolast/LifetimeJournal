package com.apptolast.lifetimejournal.data.repositories

import com.apptolast.lifetimejournal.data.auth.GoogleAuthResult
import com.apptolast.lifetimejournal.data.auth.GoogleSignInHelper
import com.apptolast.lifetimejournal.data.datamodel.User
import com.apptolast.lifetimejournal.data.datamodel.toDomain
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val googleSignInHelper: GoogleSignInHelper,
) : AuthRepository {

    private val _authState: MutableStateFlow<User?> = MutableStateFlow(null)
    override val authState: StateFlow<User?> = _authState.asStateFlow()

    override suspend fun loginWithGoogle(): Flow<Result<User>> = flow {
        try {
            // Obtener token de ID de Google usando nuestro helper
            val tokenResult = googleSignInHelper.signIn().first()

            tokenResult.fold(
                onSuccess = { googleAuthResult ->

                    when (googleAuthResult) {
                        is GoogleAuthResult.Success -> {
                            // Crear credenciales de Firebase con el token
                            val credential = googleSignInHelper.getFirebaseCredential(
                                idToken = googleAuthResult.idToken,
                                accessToken = googleAuthResult.accessToken,
                            )

                            // Iniciar sesión con Firebase usando el credential
                            val authResult = firebaseAuth.signInWithCredential(credential)

                            // Convertir FirebaseUser a nuestro modelo de dominio User
                            val user = authResult.user?.toDomain()

                            if (user != null) {
                                // Actualizar el estado de autenticación
                                _authState.value = user
                                emit(Result.success(user))
                            } else {
                                emit(Result.failure(Exception("Usuario no encontrado")))
                            }
                        }

                        is GoogleAuthResult.Failure -> {
                            emit(Result.failure(Exception(googleAuthResult.error.message)))
                        }
                    }
                },
                onFailure = { error ->
                    emit(Result.failure(error))
                },
            )
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
        _authState.update {
            it?.copy(isLoggedIn = false)
        }
    }
}

interface AuthRepository {
    val authState: StateFlow<User?>

    suspend fun loginWithGoogle(): Flow<Result<User>>

    suspend fun signOut()
}

