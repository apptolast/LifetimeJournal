package com.apptolast.lifetimejournal.core.navigation

import kotlinx.serialization.Serializable

interface Destination

@Serializable
object LoginDestination : Destination

@Serializable
object SettingDestination : Destination

@Serializable
object JournalDestination : Destination

@Serializable
data class EntriesDestination(val journalId: String?) : Destination

@Serializable
object PrivacyPolicyDestination : Destination
