package com.apptolast.lifetimejournal.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp

@Composable
fun Navigation() {
    Box(contentAlignment = Alignment.Center) {
        Text(text = "Content", fontSize = 30.sp)
    }
}
