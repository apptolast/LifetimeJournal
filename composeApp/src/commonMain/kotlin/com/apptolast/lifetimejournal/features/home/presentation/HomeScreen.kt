package com.apptolast.lifetimejournal.features.home.presentation

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
import com.apptolast.lifetimejournal.features.home.data.HomeState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeScreenRoot(viewModel: HomeViewModel = viewModel { HomeViewModel() }) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(state)
}

@Composable
fun HomeScreen(state: HomeState, modifier: Modifier = Modifier) {
    HomeContent()
}

@Composable
fun HomeContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Home")
    }
}

@Preview
@Composable
fun HomeContentPreview(modifier: Modifier = Modifier) {
    MaterialTheme {
        HomeScreen(
            state = HomeState().copy(
                isLoading = false,
            ),
        )
    }
}
