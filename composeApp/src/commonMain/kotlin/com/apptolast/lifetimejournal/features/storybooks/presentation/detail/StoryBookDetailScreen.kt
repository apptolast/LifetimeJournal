package com.apptolast.lifetimejournal.features.storybooks.presentation.detail

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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.apptolast.lifetimejournal.core.theme.LifetimeJournalTheme
import com.apptolast.lifetimejournal.data.datamodel.StoryBook
import com.apptolast.lifetimejournal.features.components.BasicTopBar
import com.apptolast.lifetimejournal.features.storybooks.data.StoryBookDetailState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault
import kotlinx.datetime.todayIn
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
fun StoryBookDetailScreenRoot(
    storyBookId: String,
    viewModel: StoryBookDetailViewModel = viewModel { StoryBookDetailViewModel() },
    onBack: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(storyBookId) {
        viewModel.init(storyBookId)
    }

    LaunchedEffect(true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                StoryBookDetailNavigationEvent.NavigateBack -> onBack()
            }
        }
    }

    StoryBookDetailScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack,
    )
}

@Composable
fun StoryBookDetailScreen(
    state: StoryBookDetailState,
    modifier: Modifier = Modifier,
    onEvent: (StoryBookDetailEvent) -> Unit = {},
    onBack: () -> Unit = {},
) {
    val storyBook = state.storyBook

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            BasicTopBar(
                title = "Story Detail",
                centerTitle = false,
                onBack = onBack,
                actions = {
                    IconButton(onClick = { onEvent(StoryBookDetailEvent.DeleteStoryBook) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }
                },
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else if (storyBook != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState()),
            ) {
                // Cover Image
                if (storyBook.coverUrl.isNotBlank()) {
                    AsyncImage(
                        model = storyBook.coverUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop,
                    )
                }
//                else {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(200.dp)
//                            .background(MaterialTheme.colorScheme.primaryContainer),
//                        contentAlignment = Alignment.Center,
//                    ) {
//                        Text(
//                            text = "📖",
//                            style = MaterialTheme.typography.displayLarge,
//                        )
//                    }
//                }

                Column(
                    modifier = Modifier.padding(16.dp),
                ) {
                    // Title
                    Text(
                        text = storyBook.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Description
                    Text(
                        text = storyBook.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Date Range
                    Text(
                        text = "${formatDate(storyBook.startDate)} – ${formatDate(storyBook.endDate)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Generated Story Text
                    Text(
                        text = storyBook.generatedText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.5,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Download PDF Button
                    Button(
                        onClick = { /* TODO: Implement PDF download */ },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                        )
                        Text(
                            text = "  Download PDF",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp),
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Share Button
                    SmallFloatingActionButton(
                        onClick = { /* TODO: Implement share */ },
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // AI Modification Section
                    Text(
                        text = "Request AI Modifications",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        OutlinedTextField(
                            value = state.modificationPrompt,
                            onValueChange = { onEvent(StoryBookDetailEvent.UpdateModificationPrompt(it)) },
                            modifier = Modifier.weight(1f),
                            placeholder = {
                                Text(
                                    text = "e.g., Make the story funnier",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontStyle = FontStyle.Italic,
                                )
                            },
                            singleLine = true,
                            shape = MaterialTheme.shapes.medium,
                            enabled = !state.isModifying,
                        )

                        FloatingActionButton(
                            onClick = { onEvent(StoryBookDetailEvent.RequestModification) },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(48.dp),
                        ) {
                            if (state.isModifying) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp,
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Send",
                                )
                            }
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

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Suppress("DEPRECATION")
private fun formatDate(date: LocalDate): String {
    val monthName = date.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
    return "$monthName ${date.dayOfMonth}, ${date.year}"
}

@OptIn(ExperimentalTime::class)
@Preview
@Composable
fun StoryBookDetailScreenPreview() {
    LifetimeJournalTheme {
        StoryBookDetailScreen(
            state = StoryBookDetailState().copy(
                storyBook = StoryBook(
                    id = "123456",
                    journalId = "journalId",
                    journalTitle = "journalTitle",
                    title = "title",
                    description = "description",
                    coverUrl = "",
                    startDate = Clock.System.todayIn(currentSystemDefault()),
                    endDate = Clock.System.todayIn(currentSystemDefault()),
                    storyStyle = "storyStyle",
                    generatedText = "generatedText",
                    createdAt = Clock.System.todayIn(currentSystemDefault()),
                ),
            ),
        )
    }
}
