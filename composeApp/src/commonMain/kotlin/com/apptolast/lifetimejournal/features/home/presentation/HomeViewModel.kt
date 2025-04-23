package com.apptolast.lifetimejournal.features.home.presentation

import androidx.lifecycle.ViewModel
import com.apptolast.lifetimejournal.features.home.data.HomeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent

class HomeViewModel :
    ViewModel(),
    KoinComponent {
    private val _state = MutableStateFlow<HomeState>(HomeState())
    val state = _state.asStateFlow()
}
