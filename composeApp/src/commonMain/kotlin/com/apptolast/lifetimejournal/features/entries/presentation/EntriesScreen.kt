package com.apptolast.lifetimejournal.features.entries.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.apptolast.lifetimejournal.core.navigation.Destination
import com.apptolast.lifetimejournal.core.theme.LifetimeJournalTheme
import com.apptolast.lifetimejournal.data.datamodel.Journal
import com.apptolast.lifetimejournal.data.datamodel.JournalEntry
import com.apptolast.lifetimejournal.features.entries.data.EntriesState
import com.apptolast.lifetimejournal.features.entries.presentation.components.AddEntryBottomSheetContent
import com.apptolast.lifetimejournal.features.entries.presentation.components.EditJournalBottomSheetContent
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EntriesScreenRoot(
    journalId: String?,
    viewModel: EntriesViewModel = viewModel { EntriesViewModel() },
    navigateTo: (Destination) -> Unit = {},
    onBack: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.init(journalId)
    }

    LaunchedEffect(true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                NavigationEvent.NavigateBack -> onBack()
            }
        }
    }

    EntriesScreen(
        state = state,
        onBack = onBack,
        onEvent = viewModel::onEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntriesScreen(
    state: EntriesState,
    modifier: Modifier = Modifier,
    onEvent: (UiEvent) -> Unit = {},
    onBack: () -> Unit = { },
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showEntryBottomSheet by remember { mutableStateOf(false) }
    var showJournalEditBottomSheet by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var entryToEdit by remember { mutableStateOf<JournalEntry?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            EntriesTopBar(
                title = state.journal?.title ?: "",
                description = state.journal?.description ?: "",
                onBack = onBack,
                onEditJournal = { showJournalEditBottomSheet = true },
                onDeleteJournal = { showDeleteConfirmation = true },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showEntryBottomSheet = true },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                )
            }
        },
        modifier = modifier,
    ) { paddingValues ->
        EntriesContent(
            entries = state.journal?.entries ?: emptyList(),
            modifier = Modifier.padding(paddingValues),
            onDeleteEntry = { entry -> onEvent(UiEvent.DeleteEntry(entry)) },
            onEditEntry = { entry ->
                entryToEdit = entry
                showEntryBottomSheet = true
            },
        )

        if (showEntryBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showEntryBottomSheet = false
                    entryToEdit = null
                },
                sheetState = sheetState,
            ) {
                AddEntryBottomSheetContent(
                    initialTitle = entryToEdit?.title ?: "",
                    initialDescription = entryToEdit?.description ?: "",
                    isEditMode = entryToEdit != null,
                    onCreateEntry = { title, description ->
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showEntryBottomSheet = false
                                val editingEntry = entryToEdit
                                if (editingEntry != null) {
                                    onEvent(UiEvent.UpdateEntry(editingEntry.copy(title = title, description = description)))
                                } else {
                                    onEvent(UiEvent.AddEntry(title, description, state.selectedDate))
                                }
                                entryToEdit = null
                            }
                        }
                    },
                )
            }
        }

        if (showJournalEditBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showJournalEditBottomSheet = false },
                sheetState = sheetState,
            ) {
                EditJournalBottomSheetContent(
                    initialTitle = state.journal?.title ?: "",
                    initialDescription = state.journal?.description ?: "",
                    onSave = { title, description ->
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showJournalEditBottomSheet = false
                                onEvent(UiEvent.UpdateJournal(title, description))
                            }
                        }
                    },
                )
            }
        }

        if (showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation = false },
                title = {
                    Text(text = "Delete Diary")
                },
                text = {
                    Text(text = "Are you sure you want to delete this diary? This action cannot be undone and all entries will be lost.")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteConfirmation = false
                            onEvent(UiEvent.DeleteJournal)
                        },
                    ) {
                        Text(
                            text = "Delete",
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirmation = false }) {
                        Text(text = "Cancel")
                    }
                },
            )
        }
    }
}

@Composable
fun EntriesContent(
    entries: List<JournalEntry>,
    modifier: Modifier = Modifier,
    onDeleteEntry: (JournalEntry) -> Unit = {},
    onEditEntry: (JournalEntry) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        if (entries.isEmpty()) {
            EntriesEmptyState(modifier = Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(entries) { entry ->
                    EntryCard(
                        entry = entry,
                        onDelete = { onDeleteEntry(entry) },
                        onEdit = { onEditEntry(entry) },
                    )
                }
            }
        }
    }
}

@Composable
private fun EntryCard(
    entry: JournalEntry,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {},
    onEdit: () -> Unit = {},
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp),
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = formatDate(entry.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = entry.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Action buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    TextButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Delete",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    TextButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Edit",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EntriesTopBar(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    onEditJournal: () -> Unit = {},
    onDeleteJournal: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (description.isNotBlank()) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onEditJournal) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit diary",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
            IconButton(onClick = onDeleteJournal) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete diary",
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = modifier,
    )
}

@Composable
private fun EntriesEmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .padding(bottom = 60.dp)
            .border(
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                shape = MaterialTheme.shapes.large,
            )
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.large,
            )
            .padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No memories yet!",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tap the '＋' button to add your first entry to this diary.",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Suppress("DEPRECATION")
private fun formatDate(date: LocalDate): String {
    val monthName = date.month.name.lowercase().replaceFirstChar { it.uppercase() }
    return "$monthName ${date.dayOfMonth}, ${date.year}"
}

@Preview
@Composable
private fun EntriesScreenPreview() {
    LifetimeJournalTheme {
        EntriesScreen(
            state = EntriesState(
                isLoading = false,
                calendarTitle = "January 2023",
                selectedDate = LocalDate(2023, 1, 1),
                journal = Journal(
                    id = "",
                    title = "Family Adventures",
                    description = "description",
                    cover = "",
                    entries = listOf(
                        JournalEntry(
                            id = "1",
                            journalId = "",
                            title = "First Day of School",
                            description = "Chloe was so excited this morning, she picked out her favorite dress and couldn't wait to...",
                            date = LocalDate(2023, 9, 5),
                        ),
                        JournalEntry(
                            id = "2",
                            journalId = "",
                            title = "Lost First Tooth",
                            description = "A visit from the tooth fairy is imminent! The wiggle is finally over and the first tooth is out...",
                            date = LocalDate(2023, 8, 15),
                        ),
                        JournalEntry(
                            id = "3",
                            journalId = "",
                            title = "Beach Day Fun",
                            description = "We built the biggest sandcastle ever today. It had towers and a moat, and we collected shells...",
                            date = LocalDate(2023, 7, 22),
                        ),
                    ),
                ),
            ),
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        )
    }
}

@Preview
@Composable
private fun EntriesScreenEmptyPreview() {
    LifetimeJournalTheme {
        EntriesScreen(
            state = EntriesState(
                isLoading = false,
                journal = Journal(
                    id = "",
                    title = "Family Adventures",
                    description = "description",
                    cover = "",
                    entries = emptyList(),
                ),
            ),
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        )
    }
}
