package com.apptolast.lifetimejournal

import androidx.compose.runtime.Composable
import com.apptolast.lifetimejournal.core.SurfaceScreen
import com.apptolast.lifetimejournal.core.navigation.Navigation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    SurfaceScreen {
        Navigation()
    }
}
