package com.apptolast.lifetimejournal.features.createjournal.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import coil3.compose.AsyncImage
import com.apptolast.lifetimejournal.core.navigation.Destination
import com.apptolast.lifetimejournal.core.navigation.EntriesDestination
import com.apptolast.lifetimejournal.core.navigation.JournalDestination
import com.apptolast.lifetimejournal.core.theme.LifetimeJournalTheme
import com.apptolast.lifetimejournal.features.components.BasicTopBar
import com.apptolast.lifetimejournal.features.createjournal.data.CreateJournalState
import com.apptolast.lifetimejournal.resources.Res
import com.apptolast.lifetimejournal.resources.create_journal_button
import com.apptolast.lifetimejournal.resources.create_journal_description
import com.apptolast.lifetimejournal.resources.create_journal_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CreateJournalScreenRoot(
    viewModel: CreateJournalViewModel = viewModel { CreateJournalViewModel() },
    navigateTo: (Destination, NavOptions) -> Unit = { _, _ -> },
    onBack: (() -> Unit)? = null,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Collect events using LaunchedEffect
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.NavigateToEntriesScreen -> {
                    navigateTo(
                        EntriesDestination(event.journal.id),
                        navOptions {
                            popUpTo(JournalDestination) {
                                inclusive = false
                            }
                        },
                    )
                }

                else -> {
                    /* no-op */
                }
            }
        }
    }

    CreateJournalScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack,
    )
}

@Composable
fun CreateJournalScreen(
    state: CreateJournalState,
    modifier: Modifier = Modifier,
    onEvent: (UiEvent) -> Unit = {},
    onBack: (() -> Unit)? = null,
) {
    Scaffold(
        topBar = {
            BasicTopBar(
                title = "Create Journal",
                onBack = onBack,
            )
        },
    ) { paddingValues ->
        CreateJournalContent(
            title = state.title,
            description = state.description,
            modifier = Modifier.padding(paddingValues),
            onEvent = onEvent,
        )
    }
}

@Composable
fun CreateJournalContent(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onEvent: (UiEvent) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AsyncImage(
                model = "", // TODO setup up random images
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .size(width = 200.dp, height = 280.dp)
                    .clip(shape = RoundedCornerShape(MaterialTheme.shapes.medium.topEnd)),
                contentScale = ContentScale.Crop,
            )

            OutlinedTextField(
                value = title,
                onValueChange = {
                    onEvent(UiEvent.OnTitleChange(it))
                },
                label = {
                    Text(
                        text = stringResource(Res.string.create_journal_title),
                        style = MaterialTheme.typography.titleSmall,
                    )
                },
                singleLine = true,
                modifier = Modifier.padding(vertical = 12.dp),
            )

            OutlinedTextField(
                value = description,
                onValueChange = {
                    onEvent(UiEvent.OnDescriptionChange(it))
                },
                label = {
                    Text(
                        text = stringResource(Res.string.create_journal_description),
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                minLines = 5,
                modifier = Modifier.padding(vertical = 12.dp),
            )
        }

        Button(
            onClick = { onEvent(UiEvent.OnCreateJournal) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        ) {
            Text(
                text = stringResource(Res.string.create_journal_button),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }
    }
}

@Preview
@Composable
private fun CreateJournalContentPreview(modifier: Modifier = Modifier) {
    LifetimeJournalTheme {
        Column(modifier = Modifier.background(color = Color.White).padding(10.dp)) {
            CreateJournalScreen(
                state = CreateJournalState().copy(
                    title = "Title",
                    description = "Description",
                    isLoading = false,
                ),
            )
        }
    }
}
