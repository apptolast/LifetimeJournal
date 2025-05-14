package com.apptolast.lifetimejournal.features.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavOptions
import com.apptolast.lifetimejournal.core.navigation.Destination
import com.apptolast.lifetimejournal.features.settings.data.SettingState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SettingScreenRoot(
    viewModel: SettingViewModel = viewModel { SettingViewModel() },
    navigateTo: (Destination, NavOptions?) -> Unit = { _, _ -> },
    navigateToLogin: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val user by viewModel.authRepository.authState.collectAsStateWithLifecycle()

    LaunchedEffect(user) {
        if (user?.isLoggedIn == false) {
            navigateToLogin()
        }
    }

    SettingScreen(
        state = state,
        signOut = viewModel::signOut,
    )
}

@Composable
fun SettingScreen(state: SettingState, modifier: Modifier = Modifier, signOut: () -> Unit = {}) {
    SettingContent(
        modifier = modifier,
        signOut = signOut,
    )
}

@Composable
fun SettingContent(modifier: Modifier = Modifier, signOut: () -> Unit = {}) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = signOut) {
            Text("Log Out")
        }
    }
}

@Preview
@Composable
private fun SettingContentPreview(modifier: Modifier = Modifier) {
    MaterialTheme {
        SettingScreen(
            state =
                SettingState().copy(
                    isLoading = false,
                ),
            modifier = modifier.background(color = Color.White),
        )
    }
}
