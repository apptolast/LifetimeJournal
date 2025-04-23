package com.apptolast.lifetimejournal.features.login.data

data class LoginState(val isLoading: Boolean = false, val isAuthenticated: Boolean = false, val error: String? = null)
