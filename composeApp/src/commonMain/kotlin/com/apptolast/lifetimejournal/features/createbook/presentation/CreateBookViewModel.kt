package com.apptolast.lifetimejournal.features.createbook.presentation

import androidx.lifecycle.ViewModel
import com.apptolast.lifetimejournal.features.createbook.data.CreateBookState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent

class CreateBookViewModel :
    ViewModel(),
    KoinComponent {

    private val _state = MutableStateFlow<CreateBookState>(CreateBookState())
    val state = _state.asStateFlow()
}
