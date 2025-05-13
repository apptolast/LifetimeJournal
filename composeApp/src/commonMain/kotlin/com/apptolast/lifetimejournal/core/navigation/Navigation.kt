package com.apptolast.lifetimejournal.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.apptolast.lifetimejournal.features.createjournal.presentation.CreateJournalScreenRoot
import com.apptolast.lifetimejournal.features.entries.presentation.EntriesScreenRoot
import com.apptolast.lifetimejournal.features.journals.presentation.JournalsScreenRoot
import com.apptolast.lifetimejournal.features.login.presentation.LoginScreenRoot
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
                navigateUp = {
                    navController.navigateUp()
                },
            )
        }

        composable<CreateJournalDestination> {
            CreateJournalScreenRoot(
                navigateTo = navController::navigate,
                onBack = { navController.popBackStack() },
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
    }
}
