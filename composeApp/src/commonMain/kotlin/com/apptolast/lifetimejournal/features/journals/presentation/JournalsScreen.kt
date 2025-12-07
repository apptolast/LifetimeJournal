package com.apptolast.lifetimejournal.features.journals.presentation

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.apptolast.lifetimejournal.core.navigation.CreateJournalDestination
import com.apptolast.lifetimejournal.core.navigation.Destination
import com.apptolast.lifetimejournal.core.navigation.EntriesDestination
import com.apptolast.lifetimejournal.core.navigation.SettingDestination
import com.apptolast.lifetimejournal.core.theme.LifetimeJournalTheme
import com.apptolast.lifetimejournal.data.datamodel.Journal
import com.apptolast.lifetimejournal.data.datamodel.User
import com.apptolast.lifetimejournal.features.journals.data.JournalsState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun JournalsScreenRoot(
    viewModel: JournalsViewModel = viewModel { JournalsViewModel() },
    navigateTo: (Destination) -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val user by viewModel.user.collectAsStateWithLifecycle()
    val journals by viewModel.journals.collectAsStateWithLifecycle()

    JournalsScreen(
        state = state,
        user = user,
        journals = journals,
        navigateTo = navigateTo,
    )
}

@Composable
fun JournalsScreen(
    state: JournalsState,
    user: User?,
    journals: List<Journal>,
    navigateTo: (Destination) -> Unit = {},
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateTo(CreateJournalDestination) },
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
    ) { paddingValues ->
        JournalsContent(
            journals = journals,
            modifier = Modifier.padding(paddingValues),
            onJournalClick = { journal ->
                navigateTo(EntriesDestination(journal.id))
            },
            onSettingsClick = { navigateTo(SettingDestination) },
        )
    }
}

@Composable
fun JournalsContent(
    journals: List<Journal>,
    modifier: Modifier = Modifier,
    onJournalClick: (Journal) -> Unit = {},
    onSettingsClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        JournalsHeader(onSettingsClick = onSettingsClick)

        Spacer(modifier = Modifier.height(16.dp))

        // Journals List or Empty State
        if (journals.isEmpty()) {
            EmptyState(modifier = Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(journals) { journal ->
                    JournalCard(
                        journal = journal,
                        onClick = { onJournalClick(journal) },
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    EmptyState()
                }
            }
        }
    }
}

@Composable
private fun JournalsHeader(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit = {},
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = "My Diaries",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Center),
        )
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier.align(Alignment.CenterEnd),
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun JournalCard(
    journal: Journal,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = journal.cover,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop,
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
            ) {
                Text(
                    text = journal.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = if (journal.entries.isNotEmpty()) {
                        "${journal.entries.size} entries"
                    } else {
                        journal.description
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
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
                text = "Your family's story begins here",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tap the '+' button below to create your first diary and start capturing precious moments.",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Preview
@Composable
private fun JournalsScreenPreview() {
    LifetimeJournalTheme {
        val color = MaterialTheme.colorScheme.primary.toArgb()
        val previewHandler = AsyncImagePreviewHandler {
            ColorImage(color)
        }

        CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
            JournalsScreen(
                state = JournalsState(isLoading = false),
                user = null,
                journals = listOf(journalMock, journalMock2, journalMock3),
            )
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Preview
@Composable
private fun JournalsScreenEmptyPreview() {
    LifetimeJournalTheme {
        val color = MaterialTheme.colorScheme.primary.toArgb()
        val previewHandler = AsyncImagePreviewHandler {
            ColorImage(color)
        }

        CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
            JournalsScreen(
                state = JournalsState(isLoading = false),
                user = null,
                journals = emptyList(),
            )
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Preview
@Composable
private fun JournalsContentPreview() {
    LifetimeJournalTheme {
        val color = MaterialTheme.colorScheme.primary.toArgb()
        val previewHandler = AsyncImagePreviewHandler {
            ColorImage(color)
        }

        CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
            JournalsContent(
                journals = listOf(journalMock, journalMock2, journalMock3),
                modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
            )
        }
    }
}

val journalMock = Journal(
    id = "1",
    title = "Family Adventures",
    description = "Last updated: Yesterday",
    cover = "https://fastly.picsum.photos/id/237/200/280.jpg",
    entries = mutableListOf(),
)

private val journalMock2 = Journal(
    id = "2",
    title = "Mommy & Me",
    description = "34 entries",
    cover = "https://fastly.picsum.photos/id/238/200/280.jpg",
    entries = mutableListOf(),
)

private val journalMock3 = Journal(
    id = "3",
    title = "Baby's First Year",
    description = "Last updated: 2 weeks ago",
    cover = "https://fastly.picsum.photos/id/239/200/280.jpg",
    entries = mutableListOf(),
)
