package com.apptolast.lifetimejournal.features.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.apptolast.lifetimejournal.features.settings.data.SettingState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SettingScreenRoot(viewModel: SettingViewModel = viewModel { SettingViewModel() }) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingScreen(state)
}

@Composable
fun SettingScreen(state: SettingState, modifier: Modifier = Modifier) {
    SettingContent()
}

@Composable
fun SettingContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Text")
    }
}

@Preview
@Composable
fun SettingContentPreview(modifier: Modifier = Modifier) {
    MaterialTheme {
        SettingScreen(
            state =
            SettingState().copy(
                isLoading = false,
            ),
        )
    }
}
