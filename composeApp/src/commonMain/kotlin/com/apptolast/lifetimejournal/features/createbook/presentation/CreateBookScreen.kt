package com.apptolast.lifetimejournal.features.createbook.presentation

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
import com.apptolast.lifetimejournal.features.createbook.data.CreateBookState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CreateBookScreenRoot(viewModel: CreateBookViewModel = viewModel { CreateBookViewModel() }) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CreateBookScreen(state)
}

@Composable
fun CreateBookScreen(state: CreateBookState, modifier: Modifier = Modifier) {
    CreateBookContent()
}

@Composable
fun CreateBookContent(modifier: Modifier = Modifier) {
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
fun CreateBookContentPreview(modifier: Modifier = Modifier) {
    MaterialTheme {
        CreateBookScreen(
            state =
            CreateBookState().copy(
                isLoading = false,
            ),
        )
    }
}
