package com.apptolast.lifetimejournal.features.settings.presentation

import androidx.lifecycle.ViewModel
import com.apptolast.lifetimejournal.features.settings.data.SettingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent

class SettingViewModel :
    ViewModel(),
    KoinComponent {
    private val _state = MutableStateFlow<SettingState>(SettingState())
    val state = _state.asStateFlow()
}
