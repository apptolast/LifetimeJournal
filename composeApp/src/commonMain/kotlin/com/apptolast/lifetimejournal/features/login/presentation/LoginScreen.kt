package com.apptolast.lifetimejournal.features.login.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.apptolast.lifetimejournal.core.navigation.Destination
import com.apptolast.lifetimejournal.core.navigation.JournalDestination
import com.apptolast.lifetimejournal.features.login.data.LoginState
import com.apptolast.lifetimejournal.resources.Res
import com.apptolast.lifetimejournal.resources.google_icon
import com.apptolast.lifetimejournal.resources.login_google_button
import com.apptolast.lifetimejournal.resources.login_loading_text
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = viewModel { LoginViewModel() },
    navigateTo: (Destination) -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = state.isAuthenticated) {
        if (state.isAuthenticated) {
            navigateTo(JournalDestination)
        }
    }

    LoginScreen(
        state = state,
        onClickGoogleButton = viewModel::loginWithGoogle,
        navigateTo = navigateTo,
    )
}

@Composable
fun LoginScreen(state: LoginState, onClickGoogleButton: () -> Unit = {}, navigateTo: (Destination) -> Unit = {}) {
    LoginContent(
        authState = state.isAuthenticated,
        modifier = Modifier,
        onClickGoogleButton = onClickGoogleButton,
    )
}

@Composable
fun LoginContent(authState: Boolean, modifier: Modifier = Modifier, onClickGoogleButton: () -> Unit = {}) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Add
        SignInWithGoogleButton(
            onClick = onClickGoogleButton,
        )
    }
}

@Composable
fun SignInWithGoogleButton(
    text: String = stringResource(Res.string.login_google_button),
    loadingText: String = stringResource(Res.string.login_loading_text),
    icon: Painter = painterResource(Res.drawable.google_icon), // Replace with your actual Google logo resource
    isLoading: Boolean = false,
    onClick: () -> Unit,
) {
    Surface(
        modifier =
        Modifier
            .clickable(
                enabled = !isLoading,
                onClick = onClick,
            ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier =
            Modifier
                .padding(
                    start = 12.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 12.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = icon,
                contentDescription = "Google Button",
                modifier = Modifier.size(24.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = if (isLoading) loadingText else text)
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

// @Preview(showBackground = true)
// @Composable
// fun SignInWithGoogleButtonPreview() {
//    SignInWithGoogleButton(onClick = {})
// }
