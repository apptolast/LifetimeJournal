package com.apptolast.lifetimejournal.features.settings.data

import com.apptolast.lifetimejournal.data.datamodel.User

data class SettingState(
    val isLoading: Boolean = false,
    val user: User? = null,
)
