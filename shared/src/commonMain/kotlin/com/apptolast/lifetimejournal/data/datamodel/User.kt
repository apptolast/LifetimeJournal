package com.apptolast.lifetimejournal.data.datamodel

import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val photoUrl: String,
    val phoneNumber: String,
    val isEmailVerified: Boolean,
    val isLoggedIn: Boolean,
)

fun FirebaseUser?.toDomain() = User(
    id = this?.uid ?: "",
    name = this?.displayName ?: "",
    email = this?.email ?: "",
    photoUrl = this?.photoURL ?: "",
    phoneNumber = this?.phoneNumber ?: "",
    isEmailVerified = this?.isEmailVerified ?: false,
    isLoggedIn = this != null,
)

/**
 * Extensión para convertir FirebaseUser a nuestro modelo de dominio User
 */
//private fun FirebaseUser.toDomainUser(): User {
//    return User(
//        id = uid,
//        name = displayName ?: "",
//        email = email ?: "",
//        photoUrl = photoURL ?: "",
//        phoneNumber = phoneNumber ?: "",
//        isEmailVerified = isEmailVerified,
//        isLoggedIn = true,
//    )
//}
