package com.apptolast.lifetimejournal.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.apptolast.lifetimejournal.features.createbook.presentation.CreateBookScreenRoot
import com.apptolast.lifetimejournal.features.home.presentation.HomeScreenRoot
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
            HomeScreenRoot {
                navController.navigate(it)
            }
        }

        composable<SettingDestination> {
            SettingScreenRoot()
        }

        composable<CreateBookDestination> {
            CreateBookScreenRoot()
        }
    }
}
