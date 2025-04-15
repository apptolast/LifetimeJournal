package com.apptolast.lifetimejournal.features.login.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.apptolast.lifetimejournal.core.navigation.Destination
import com.apptolast.lifetimejournal.core.navigation.HomeDestination
import com.apptolast.lifetimejournal.features.login.data.LoginState

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = viewModel { LoginViewModel() },
    navigateTo: (Destination) -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LoginScreen(
        state = state,
        navigateTo = navigateTo,
    )
}

@Composable
fun LoginScreen(state: LoginState, navigateTo: (Destination) -> Unit = {}) {
    LoginContent(
        modifier = Modifier,
        navigateTo = navigateTo,
    )
}

@Composable
fun LoginContent(modifier: Modifier = Modifier, navigateTo: (Destination) -> Unit = {}) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Login")
        Button(
            onClick = { navigateTo(HomeDestination) },
        ) {
            Text("Go to Home")
        }
    }
}

// @Preview
// @Composable
// fun LoginContentPreview(modifier: Modifier = Modifier) {
//    MaterialTheme {
//        LoginScreen(
//            state = LoginState().copy(
//                isLoading = false,
//            ),
//        )
//    }
// }
//
