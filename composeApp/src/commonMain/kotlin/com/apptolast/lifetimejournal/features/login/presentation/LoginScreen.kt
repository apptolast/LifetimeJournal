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
        authState = state.isAuthenticated,
        modifier = Modifier,
        navigateTo = navigateTo,
    )
}

@Composable
fun LoginContent(
    authState: Boolean,
    modifier: Modifier = Modifier,
    navigateTo: (Destination) -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Login $authState")
        Button(
            onClick = { navigateTo(HomeDestination) },
        ) {
            Text("Login")
        }
    }
}

//@Composable
//fun SignInWithGoogleButton(
//    text: String = "Sign in with Google",
//    loadingText: String = "Signing in...",
//    icon: Painter = painterResource(id = R.drawable.ic_google_logo), // Replace with your actual Google logo resource
//    isLoading: Boolean = false,
//    onClick: () -> Unit
//) {
//    Surface(
//        modifier = Modifier
//            .clickable(
//                enabled = !isLoading,
//                onClick = onClick
//            ),
//        shape = RoundedCornerShape(12.dp),
//        border = BorderStroke(width = 1.dp, color = Color.LightGray),
//        color = MaterialTheme.colorScheme.surface
//    ) {
//        Row(
//            modifier = Modifier
//                .padding(
//                    start = 12.dp,
//                    end = 16.dp,
//                    top = 12.dp,
//                    bottom = 12.dp
//                ),
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Image(
//                painter = icon,
//                contentDescription = "Google Button",
//                modifier = Modifier.size(24.dp)
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(text = if (isLoading) loadingText else text)
//        }
//    }
//}

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

//@Preview(showBackground = true)
//@Composable
//fun SignInWithGoogleButtonPreview() {
//    SignInWithGoogleButton(onClick = {})
//}
