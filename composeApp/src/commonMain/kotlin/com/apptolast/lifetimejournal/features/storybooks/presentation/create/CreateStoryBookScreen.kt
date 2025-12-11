package com.apptolast.lifetimejournal.features.storybooks.presentation.create

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.apptolast.lifetimejournal.core.navigation.Destination
import com.apptolast.lifetimejournal.core.navigation.StoryBookDetailDestination
import com.apptolast.lifetimejournal.core.theme.LifetimeJournalTheme
import com.apptolast.lifetimejournal.data.datamodel.Journal
import com.apptolast.lifetimejournal.features.components.BasicTopBar
import com.apptolast.lifetimejournal.features.storybooks.data.CreateStoryBookState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CreateStoryBookScreenRoot(
    viewModel: CreateStoryBookViewModel = viewModel { CreateStoryBookViewModel() },
    navigateTo: (Destination) -> Unit = {},
    onBack: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is CreateStoryBookNavigationEvent.NavigateToDetail -> {
                    navigateTo(StoryBookDetailDestination(event.storyBookId))
                }
            }
        }
    }

    CreateStoryBookScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateStoryBookScreen(
    state: CreateStoryBookState,
    modifier: Modifier = Modifier,
    onEvent: (CreateStoryBookEvent) -> Unit = {},
    onBack: () -> Unit = {},
) {
    val dateRangePickerState = rememberDateRangePickerState()

    LaunchedEffect(dateRangePickerState.selectedStartDateMillis, dateRangePickerState.selectedEndDateMillis) {
        onEvent(
            CreateStoryBookEvent.SelectDateRange(
                startMillis = dateRangePickerState.selectedStartDateMillis,
                endMillis = dateRangePickerState.selectedEndDateMillis,
            ),
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            BasicTopBar(
                title = "Create Your Storybook",
                centerTitle = false,
                onBack = onBack,
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
            ) {
                Button(
                    onClick = { onEvent(CreateStoryBookEvent.GenerateBook) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.selectedJournal != null &&
                        state.startDateMillis != null &&
                        state.endDateMillis != null &&
                        !state.isGenerating,
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) {
                    if (state.isGenerating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Text(
                            text = "Generate Book",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp),
                        )
                    }
                }

                if (state.error != null) {
                    Text(
                        text = state.error,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            }
        },
        modifier = modifier,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            // Step 1: Choose a Diary
            StepHeader(number = 1, title = "Choose a Diary")
            Spacer(modifier = Modifier.height(12.dp))
            JournalDropdown(
                journals = state.journals,
                selectedJournal = state.selectedJournal,
                onJournalSelected = { onEvent(CreateStoryBookEvent.SelectJournal(it)) },
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Step 2: Select a Date Range
            StepHeader(number = 2, title = "Select a Date Range")
            Spacer(modifier = Modifier.height(12.dp))

            DateRangePicker(
                state = dateRangePickerState,
                modifier = Modifier.height(340.dp).clip(shape = MaterialTheme.shapes.extraLarge),
                title = null,
                headline = null,
                showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                    selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
                    todayContentColor = MaterialTheme.colorScheme.primary,
                    todayDateBorderColor = MaterialTheme.colorScheme.primary,
                    dayInSelectionRangeContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                    dayInSelectionRangeContentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Step 3: Describe the Story Style
            StepHeader(number = 3, title = "Describe the Story Style")

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.storyStyle,
                onValueChange = { onEvent(CreateStoryBookEvent.UpdateStoryStyle(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "e.g., 'like a children's fairy tale', 'an adventurous pirate story'.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                minLines = 3,
                shape = MaterialTheme.shapes.medium,
            )

            Text(
                text = "Our AI will use this to set the tone of your book.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StepHeader(
    number: Int,
    title: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "①②③④⑤⑥⑦⑧⑨⑩"[number - 1].toString(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = " $title",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun JournalDropdown(
    journals: List<Journal>,
    selectedJournal: Journal?,
    modifier: Modifier = Modifier,
    onJournalSelected: (Journal) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.medium,
                )
                .clickable { expanded = true }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = selectedJournal?.title ?: "Select a diary",
                style = MaterialTheme.typography.bodyLarge,
                color = if (selectedJournal != null) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f),
        ) {
            journals.forEach { journal ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = journal.title,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    onClick = {
                        onJournalSelected(journal)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun StoryBooksListScreenPreview() {
    LifetimeJournalTheme {
        CreateStoryBookScreen(
            state = CreateStoryBookState(),
        )
    }
}
