package com.apptolast.lifetimejournal.core.navigation

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.apptolast.lifetimejournal.features.components.BottomNavigationBar
import com.apptolast.lifetimejournal.features.entries.presentation.EntriesScreenRoot
import com.apptolast.lifetimejournal.features.journals.presentation.JournalsScreenRoot
import com.apptolast.lifetimejournal.features.login.presentation.LoginScreenRoot
import com.apptolast.lifetimejournal.features.privacypolicy.presentation.PrivacyPolicyScreenRoot
import com.apptolast.lifetimejournal.features.settings.presentation.SettingScreenRoot
import com.apptolast.lifetimejournal.features.storybooks.presentation.create.CreateStoryBookScreenRoot
import com.apptolast.lifetimejournal.features.storybooks.presentation.detail.StoryBookDetailScreenRoot
import com.apptolast.lifetimejournal.features.storybooks.presentation.list.StoryBooksListScreenRoot

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Routes that should show bottom navigation
    val showBottomNav = currentDestination?.let { dest ->
        dest.hasRoute(JournalDestination::class) ||
            dest.hasRoute(StoryBooksListDestination::class) ||
            dest.hasRoute(SettingDestination::class)
    } ?: false

    val currentRoute: Destination = when {
        currentDestination?.hasRoute(JournalDestination::class) == true -> JournalDestination
        currentDestination?.hasRoute(StoryBooksListDestination::class) == true -> StoryBooksListDestination
        currentDestination?.hasRoute(SettingDestination::class) == true -> SettingDestination
        else -> JournalDestination
    }

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    navigateTo = { destination ->
                        navController.navigate(destination) {
                            popUpTo(JournalDestination) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = LoginDestination,
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues),
        ) {
            composable<LoginDestination> {
                LoginScreenRoot(
                    navigateTo = { destination ->
                        navController.navigate(destination) {
                            popUpTo(LoginDestination) { inclusive = true }
                        }
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
                    onBack = null,
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

            // Story Books screens
            composable<StoryBooksListDestination> {
                StoryBooksListScreenRoot(
                    navigateTo = { destination ->
                        navController.navigate(destination)
                    },
                )
            }

            composable<CreateStoryBookDestination> {
                CreateStoryBookScreenRoot(
                    navigateTo = { destination ->
                        navController.navigate(destination) {
                            popUpTo(StoryBooksListDestination) {
                                inclusive = false
                            }
                        }
                    },
                    onBack = { navController.popBackStack() },
                )
            }

            composable<StoryBookDetailDestination> { backStackEntry ->
                val (storyBookId) = backStackEntry.toRoute<StoryBookDetailDestination>()
                StoryBookDetailScreenRoot(
                    storyBookId = storyBookId,
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}
