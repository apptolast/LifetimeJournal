package com.apptolast.lifetimejournal.previews

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.apptolast.lifetimejournal.features.login.data.LoginState
import com.apptolast.lifetimejournal.features.login.presentation.LoginScreen

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, locale = "es")
@Composable
fun LoginContentPreview() {
    MaterialTheme {
        LoginScreen(
            state = LoginState().copy(
                isLoading = false,
            ),
            user = null,
        )
    }
}
