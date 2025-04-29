package com.apptolast.lifetimejournal.features.createjournal.data

data class CreateJournalState(val isLoading: Boolean = false, val title: String = "", val description: String = "")

sealed interface UiEvent {
    data class OnTitleChange(val value: String) : UiEvent
    data class OnDescriptionChange(val value: String) : UiEvent
}
