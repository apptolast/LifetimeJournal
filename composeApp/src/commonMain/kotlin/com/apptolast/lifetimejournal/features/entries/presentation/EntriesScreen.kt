package com.apptolast.lifetimejournal.features.entries.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.apptolast.lifetimejournal.core.navigation.Destination
import com.apptolast.lifetimejournal.core.theme.LifetimeJournalTheme
import com.apptolast.lifetimejournal.data.datamodel.Journal
import com.apptolast.lifetimejournal.data.datamodel.JournalEntry
import com.apptolast.lifetimejournal.features.components.BasicTopBar
import com.apptolast.lifetimejournal.features.entries.data.EntriesState
import com.apptolast.lifetimejournal.features.entries.presentation.components.AddEntryBottomSheetContent
import com.apptolast.lifetimejournal.resources.Res
import com.apptolast.lifetimejournal.resources.entries_add_entry_fab_button
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.minusDays
import com.kizitonwose.calendar.core.plusDays
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EntriesScreenRoot(
    journalId: Long?,
    viewModel: EntriesViewModel = viewModel { EntriesViewModel() },
    navigateTo: (Destination) -> Unit = {},
    onBack: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.init(journalId)
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
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            BasicTopBar(
                title = state.calendarTitle,
                centerTitle = false,
                onBack = onBack,
                actions = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
//                    onEvent(UiEvent.AddEntry(state.selectedDate))
                    showBottomSheet = true
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(Res.string.entries_add_entry_fab_button),
                    )
                },
                text = { Text(stringResource(Res.string.entries_add_entry_fab_button)) },
                modifier = Modifier.padding(vertical = 16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        EntriesContent(
            entries = state.journal?.entries ?: emptyList(),
            selectedDate = state.selectedDate,
            modifier = Modifier.padding(paddingValues),
            onEvent = onEvent,
        )

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
            ) {
                AddEntryBottomSheetContent(
                    onCreateEntry = { title, description ->
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                                onEvent(UiEvent.AddEntry(title, description, state.selectedDate))
                            }
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun EntriesContent(
    entries: List<JournalEntry> = emptyList(),
    selectedDate: LocalDate,
    modifier: Modifier = Modifier,
    onEvent: (UiEvent) -> Unit = {},
) {
    val startDate = remember { selectedDate.minusDays(200) }
    val endDate = remember { selectedDate.plusDays(200) }
    var selection by remember { mutableStateOf(selectedDate) }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        val state = rememberWeekCalendarState(
            startDate = startDate,
            endDate = endDate,
            firstVisibleWeekDate = selectedDate,
        )

        val visibleWeek = rememberFirstVisibleWeekAfterScroll(state)
        val title =
            "${visibleWeek.days.first().date.month.name} ${visibleWeek.days.first().date.year}"
        onEvent(UiEvent.CalendarTitle(title))

        WeekCalendar(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.primary),
            state = state,
            dayContent = { day ->
                Day(
                    date = day.date,
                    isSelected = selection == day.date,
                    onClick = { date ->
                        if (selection != date) {
                            selection = date
                            onEvent(UiEvent.SelectDate(date))
                        }
                    },
                )
            },
        )

        EntriesContent(entries = entries)
    }
}

@Composable
private fun Day(date: LocalDate, isSelected: Boolean, onClick: (LocalDate) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick(date) },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = date.dayOfWeek.name.substring(0..2),
                fontSize = 12.sp,
                color = Color.White,
                fontWeight = FontWeight.Light,
            )
            Text(
                text = "${date.dayOfMonth}",
                fontSize = 14.sp,
                color = if (isSelected) Color.White else Color.LightGray,
                fontWeight = FontWeight.Bold,
            )
        }
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(Color.Yellow)
                    .align(Alignment.BottomCenter),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntriesContent(entries: List<JournalEntry>, modifier: Modifier = Modifier) {
    val expandedStates = remember { mutableStateMapOf<Int, Boolean>() }

    LazyColumn(
        modifier = modifier.padding(8.dp),
        contentPadding = PaddingValues(12.dp),
    ) {
        itemsIndexed(entries) { index, entry ->

            val isExpanded = expandedStates[index] ?: false
            CompositionLocalProvider(LocalRippleConfiguration provides null) {
                Column(
                    modifier = Modifier
                        .clip(RectangleShape)
                        .clickable {
                            expandedStates[index] = !isExpanded
                        }
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessMedium,
                            ),
                        ),
                ) {
                    Text(
                        text = entry.title,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 2.dp,
                        ),
                    )

                    Text(
                        text = "${entry.date}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 2.dp,
                        ),
                    )

                    Text(
                        text = entry.description,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(
                                horizontal = 10.dp,
                                vertical = 2.dp,
                            ),
                    )

                    // Show the divider only if it's not the last item
                    if (index < entries.lastIndex) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color.LightGray,
                            modifier = Modifier.padding(12.dp),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun EntriesContentPreview() {
    LifetimeJournalTheme {
        EntriesScreen(
            state = EntriesState().copy(
                isLoading = false,
                calendarTitle = "January 2023",
                selectedDate = LocalDate(2023, 1, 1),
                journal = Journal(
                    title = "title",
                    description = "description",
                    cover = "",
                    entries = mutableListOf(
                        JournalEntry(
                            id = 0,
                            title = "title 1",
                            description = "des 1",
                            date = LocalDate(2023, 1, 1),
                        ),
                        JournalEntry(
                            id = 0,
                            title = "title 2",
                            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                            date = LocalDate(2023, 6, 12),
                        ),
                        JournalEntry(
                            id = 0,
                            title = "title 3",
                            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                            date = LocalDate(2023, 6, 12),
                        ),
                    ),
                ),
            ),
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        )
    }
}

/**
 * Find first visible week in a paged week calendar **after** scrolling stops.
 */
@Composable
fun rememberFirstVisibleWeekAfterScroll(state: WeekCalendarState): Week {
    val visibleWeek = remember(state) { mutableStateOf(state.firstVisibleWeek) }
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .filter { scrolling -> !scrolling }
            .collect { visibleWeek.value = state.firstVisibleWeek }
    }
    return visibleWeek.value
}

// Known issue in iOS doing the scroll animation
// https://youtrack.jetbrains.com/issue/CMP-8030/Uncaught-Kotlin-exception-kotlin.native.internal.IrLinkageError
