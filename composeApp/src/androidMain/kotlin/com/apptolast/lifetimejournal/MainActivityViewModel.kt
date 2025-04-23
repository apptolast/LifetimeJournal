package com.apptolast.lifetimejournal

import androidx.lifecycle.ViewModel
import com.apptolast.lifetimejournal.repositories.AuthRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainActivityViewModel :
    ViewModel(),
    KoinComponent {

    val authRepository: AuthRepository by inject()

    val authState = authRepository.authState
}
