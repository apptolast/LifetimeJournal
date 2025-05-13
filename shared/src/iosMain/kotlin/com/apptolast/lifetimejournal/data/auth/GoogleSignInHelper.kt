package com.apptolast.lifetimejournal.data.auth

//import cocoapods.GoogleSignIn.signInWithConfigurationPresentingViewControllerCallback
//import platform.UIKit.UISceneState
import cocoapods.GoogleSignIn.GIDSignIn
import dev.gitlive.firebase.auth.AuthCredential
import dev.gitlive.firebase.auth.GoogleAuthProvider
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.UIKit.UIApplication

@OptIn(ExperimentalForeignApi::class)
actual class GoogleSignInHelper {

    /**
     * Inicia el proceso de inicio de sesión con Google y devuelve un token de ID.
     *
     * Esta función devuelve un flujo que emite un resultado con el token de ID o un error.
     * Si el usuario cancela el inicio de sesión, el flujo se cierra sin emitir nada.
     *
     * @return Flujo que emite el resultado con el token ID o un error
     */
    actual suspend fun signIn(): Flow<Result<GoogleAuthResult>> = callbackFlow {
        // Obtener el controlador de vista actual para presentar la interfaz de inicio de sesión
        val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController

        if (viewController == null) {
            trySend(Result.failure(Exception("No se pudo encontrar el UIViewController")))
            close()
            return@callbackFlow
        }

        GIDSignIn.sharedInstance.signInWithPresentingViewController(viewController) { gidSignInResult, nsError ->
            nsError?.let { println("Error While signing: $nsError") }

            // Obtener el token ID del usuario


            val idToken = gidSignInResult?.user?.idToken?.tokenString
            val accessToken = gidSignInResult?.user?.accessToken?.tokenString

            if (nsError != null) {
                trySend(Result.failure(Exception(nsError.localizedDescription)))
            } else if (idToken != null) {
                trySend(
                    Result.success(
                        GoogleAuthResult.Success(
                            idToken = idToken,
                            accessToken = accessToken,
                        ),
                    ),
                )
            } else {
                trySend(Result.failure(Exception("No se pudo obtener el token ID")))
            }
            close()
        }

        awaitClose()
    }

    actual fun getFirebaseCredential(idToken: String, accessToken: String?): AuthCredential {
        return GoogleAuthProvider.credential(idToken, accessToken)
    }

}

