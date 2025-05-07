package com.apptolast.lifetimejournal

import androidx.lifecycle.ViewModel
import com.apptolast.lifetimejournal.data.repositories.AuthRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainActivityViewModel :
    ViewModel(),
    KoinComponent {

    val authRepository: AuthRepository by inject()

    val authState = authRepository.authState
}
