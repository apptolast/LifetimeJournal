package com.apptolast.lifetimejournal.features.journals.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import coil3.compose.LocalPlatformContext
import com.apptolast.lifetimejournal.core.navigation.CreateJournalDestination
import com.apptolast.lifetimejournal.core.navigation.Destination
import com.apptolast.lifetimejournal.core.navigation.EntriesDestination
import com.apptolast.lifetimejournal.core.theme.LifetimeJournalTheme
import com.apptolast.lifetimejournal.data.datamodel.Journal
import com.apptolast.lifetimejournal.data.datamodel.User
import com.apptolast.lifetimejournal.features.components.BottomNavigationBar
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
        bottomBar = {
            BottomNavigationBar(navigateTo = navigateTo)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateTo(CreateJournalDestination) },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary, // Use contentColor for icon/text color
            ) {
                Icon(
                    imageVector = Icons.Filled.Add, // Use a standard icon
                    contentDescription = null, // Add content description
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(38.dp),
                )
            }
        },
    ) { paddingValues ->
        JournalsContent(
            user = user,
            journals = journals,
            modifier = Modifier.padding(paddingValues),
            onJournalClick = { journal ->
                navigateTo(EntriesDestination(journal.id))
            },
        )
    }
}

@Composable
fun JournalsContent(
    user: User?,
    journals: List<Journal>,
    modifier: Modifier = Modifier,
    onJournalClick: (Journal) -> Unit = {},
) {
    Column(modifier = modifier.fillMaxSize().padding(18.dp)) {
        Header(user = user)

        // Books list, possible horizontal scroll to change between them @krastev
        BookInfo(
            journals = journals,
            modifier = Modifier.weight(1f),
            onJournalClick = onJournalClick,
        )
    }
}

@Composable
fun Header(user: User?, modifier: Modifier = Modifier) {
    val context = LocalPlatformContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = "Bienvenido/a",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = user?.name ?: "",
                style = MaterialTheme.typography.headlineSmall,
            )
        }

        AsyncImage(
            model = user?.photoUrl,
            contentDescription = null,
            modifier = Modifier.padding(horizontal = 16.dp).clip(CircleShape).size(60.dp),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
fun BookInfo(journals: List<Journal>, modifier: Modifier = Modifier, onJournalClick: (Journal) -> Unit = {}) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        contentPadding = PaddingValues(8.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        items(journals) { journal ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .width(250.dp)
                    .clickable { onJournalClick(journal) },
            ) {
                AsyncImage(
                    model = journal.cover,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(width = 200.dp, height = 280.dp)
                        .clip(shape = RoundedCornerShape(MaterialTheme.shapes.medium.topEnd)),
                    contentScale = ContentScale.Crop,
                )
                Text(
                    text = journal.title,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                )
                Text(
                    text = journal.description,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    minLines = 5,
                    maxLines = 5,
                )
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Preview
@Composable
private fun JournalsContentPreview() {
    LifetimeJournalTheme {
        Column(modifier = Modifier.background(color = Color.White).padding(10.dp)) {
            val color = MaterialTheme.colorScheme.primary.toArgb()
            val previewHandler = AsyncImagePreviewHandler {
                ColorImage(color)
            }

            CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
                JournalsScreen(
                    state = JournalsState().copy(isLoading = false),
                    user = null,
                    journals = listOf(journalMock, journalMock),
                )
            }
        }
    }
}

val journalMock = Journal(
    id = 0,
    title = "The Alchemist",
    description = "A novel by Brazilian author Paulo Coelho is a classic of modern literature.",
    cover = "https://fastly.picsum.photos/id/237/200/280.jpg?hmac=w-Mx-kWY0n3hE8oWamWigvnDWnsyAUzM6haQAlzNqZE",
    entries = mutableListOf(),
)
