package com.apptolast.lifetimejournal.core.navigation

import kotlinx.serialization.Serializable

interface Destination

@Serializable
object LoginDestination : Destination

@Serializable
object SettingDestination : Destination

@Serializable
object CreateBookDestination : Destination

@Serializable
object HomeDestination : Destination

// @Serializable
// data class DetailDestination {
//    val movie: Movie
// }
