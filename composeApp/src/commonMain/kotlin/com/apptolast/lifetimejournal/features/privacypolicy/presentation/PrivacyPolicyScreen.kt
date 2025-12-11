package com.apptolast.lifetimejournal.features.privacypolicy.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.apptolast.lifetimejournal.features.components.BasicTopBar
import com.apptolast.lifetimejournal.resources.Res
import com.apptolast.lifetimejournal.resources.settings_privacy_policy
import org.jetbrains.compose.resources.stringResource

private const val PRIVACY_POLICY_URL = "https://albertohidalgo.apptolast.com/privacy/lifetime-journal"

@Composable
fun PrivacyPolicyScreenRoot(
    onBack: () -> Unit = {},
) {
    PrivacyPolicyScreen(
        url = PRIVACY_POLICY_URL,
        onBack = onBack,
    )
}

@Composable
fun PrivacyPolicyScreen(
    url: String,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            BasicTopBar(
                title = stringResource(Res.string.settings_privacy_policy),
                onBack = onBack,
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            WebViewComposable(
                url = url,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
expect fun WebViewComposable(url: String, modifier: Modifier = Modifier)
