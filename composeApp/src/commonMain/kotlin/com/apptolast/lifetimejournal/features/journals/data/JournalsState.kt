package com.apptolast.lifetimejournal.features.journals.data

import com.apptolast.lifetimejournal.domain.Journal

data class JournalsState(val journals: List<Journal> = emptyList(), val isLoading: Boolean = false)
