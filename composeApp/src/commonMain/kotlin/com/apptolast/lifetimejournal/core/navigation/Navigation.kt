package com.apptolast.lifetimejournal.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.apptolast.lifetimejournal.features.entries.presentation.EntriesScreenRoot
import com.apptolast.lifetimejournal.features.journals.presentation.JournalsScreenRoot
import com.apptolast.lifetimejournal.features.login.presentation.LoginScreenRoot
import com.apptolast.lifetimejournal.features.privacypolicy.presentation.PrivacyPolicyScreenRoot
import com.apptolast.lifetimejournal.features.settings.presentation.SettingScreenRoot

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginDestination,
        modifier = Modifier,
    ) {
        composable<LoginDestination> {
            LoginScreenRoot(
                navigateTo = { destination ->
                    navController.navigate(destination)
                },
            )
        }

        composable<JournalDestination> {
            JournalsScreenRoot {
                navController.navigate(it)
            }
        }

        composable<SettingDestination> {
            SettingScreenRoot(
                navigateTo = navController::navigate,
                navigateToLogin = {
                    navController.navigate(LoginDestination) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onBack = navController::popBackStack,
                onPrivacyPolicy = {
                    navController.navigate(PrivacyPolicyDestination)
                },
            )
        }

        composable<EntriesDestination> { backStackEntry ->
            val (journalId) = backStackEntry.toRoute<EntriesDestination>()
            EntriesScreenRoot(
                journalId = journalId,
                navigateTo = { destination ->
                    navController.navigate(destination)
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable<PrivacyPolicyDestination> {
            PrivacyPolicyScreenRoot(
                onBack = { navController.popBackStack() },
            )
        }
    }
}
