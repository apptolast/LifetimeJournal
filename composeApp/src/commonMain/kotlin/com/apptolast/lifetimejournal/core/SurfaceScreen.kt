package com.apptolast.lifetimejournal.core

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.apptolast.lifetimejournal.core.theme.LifetimeJournalTheme

@Composable
fun SurfaceScreen(content: @Composable () -> Unit) {
    LifetimeJournalTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            content = content,
        )
    }
}
