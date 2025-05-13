package com.apptolast.lifetimejournal.data.auth

import dev.gitlive.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.Flow

@Suppress("KotlinNoActualForExpect", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class GoogleSignInHelper() {
    /**
     * Starts the Google sign-in process and returns an ID token.
     * @return Flow with Result containing the ID token or an error
     */
    suspend fun signIn(): Flow<Result<GoogleAuthResult>>

    /**
     * Obtains Google credentials for Firebase from the ID token
     * @param idToken Google ID token
     * @return Credentials for Firebase
     */
    fun getFirebaseCredential(idToken: String, accessToken: String? = null): AuthCredential
}

sealed interface GoogleAuthResult {
    data class Success(val idToken: String, val accessToken: String?) : GoogleAuthResult
    data class Failure(val error: Throwable) : GoogleAuthResult
}
