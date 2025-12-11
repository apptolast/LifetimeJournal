package com.apptolast.lifetimejournal.features.storybooks.data

import com.apptolast.lifetimejournal.data.datamodel.StoryBook

data class StoryBooksListState(
    val isLoading: Boolean = false,
    val storyBooks: List<StoryBook> = emptyList(),
    val error: String? = null,
)
