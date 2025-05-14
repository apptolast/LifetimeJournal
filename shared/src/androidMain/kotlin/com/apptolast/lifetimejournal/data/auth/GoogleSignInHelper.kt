package com.apptolast.lifetimejournal.data.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.apptolast.lifetimejournal.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dev.gitlive.firebase.auth.AuthCredential
import dev.gitlive.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class GoogleSignInHelper : KoinComponent {

    private val context: Context by inject()

    // Configura la solicitud de credenciales una vez y reutilízala
    private val credentialRequest: GetCredentialRequest by lazy {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(BuildConfig.WEB_ID_CLIENT)
            .build()

        GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    /**
     * Inicia sesión con Google usando la Credential API
     * @param context Contexto de Android necesario para la Credential API
     * @return Flow que emite el resultado con el token ID o un error
     */
    actual suspend fun signIn(): Flow<Result<GoogleAuthResult>> = callbackFlow {
        try {
            val credentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(
                request = credentialRequest,
                context = context,
            )

            handleSignInResult(result, this)
        } catch (e: GetCredentialException) {
            trySend(Result.failure(e))
        } catch (e: Exception) {
            trySend(Result.failure(e))
        }

        awaitClose()
    }

    private fun handleSignInResult(
        result: GetCredentialResponse,
        flow: ProducerScope<Result<GoogleAuthResult>>,
    ) {
        val credential = result.credential
        when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        flow.trySend(
                            Result.success(
                                GoogleAuthResult.Success(
                                    idToken = googleIdTokenCredential.idToken,
                                    accessToken = null,
                                ),
                            ),
                        )
                    } catch (e: GoogleIdTokenParsingException) {
                        flow.trySend(Result.failure(e))
                    }
                } else {
                    flow.trySend(Result.failure(Exception("Tipo de credencial inesperado")))
                }
            }

            else -> {
                flow.trySend(Result.failure(Exception("Tipo de credencial inesperado")))
            }
        }
    }

    actual fun getFirebaseCredential(idToken: String, accessToken: String?): AuthCredential {
        return GoogleAuthProvider.credential(idToken, accessToken)
    }
}
