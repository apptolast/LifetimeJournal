package com.apptolast.lifetimejournal.features.storybooks.data

import com.apptolast.lifetimejournal.data.datamodel.StoryBook

data class StoryBookDetailState(
    val isLoading: Boolean = false,
    val storyBook: StoryBook? = null,
    val error: String? = null,
    val modificationPrompt: String = "",
    val isModifying: Boolean = false,
)
