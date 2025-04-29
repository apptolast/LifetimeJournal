package com.apptolast.lifetimejournal.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.apptolast.lifetimejournal.features.createjournal.presentation.CreateJournalScreenRoot
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

        composable<HomeDestination> {
            JournalsScreenRoot {
                navController.navigate(it)
            }
        }

        composable<SettingDestination> {
            SettingScreenRoot()
        }

        composable<CreateJournalDestination> {
            CreateJournalScreenRoot(
                onBack = { navController.popBackStack() },
            )
        }
    }
}
