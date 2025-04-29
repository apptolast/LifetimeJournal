package com.apptolast.lifetimejournal.features.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.apptolast.lifetimejournal.core.navigation.CreateJournalDestination
import com.apptolast.lifetimejournal.core.navigation.Destination
import com.apptolast.lifetimejournal.core.navigation.HomeDestination
import com.apptolast.lifetimejournal.core.navigation.SettingDestination
import com.apptolast.lifetimejournal.core.theme.LifetimeJournalTheme
import com.apptolast.lifetimejournal.resources.Res
import com.apptolast.lifetimejournal.resources.bottom_nav_books_label
import com.apptolast.lifetimejournal.resources.bottom_nav_home_label
import com.apptolast.lifetimejournal.resources.bottom_nav_profile_label
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

sealed class BottomNavItem(val route: Destination, val icon: ImageVector, val label: StringResource) {
    object Home : BottomNavItem(
        route = HomeDestination,
        icon = Icons.Default.Home,
        label = Res.string.bottom_nav_home_label,
    )

    object CreateBook : BottomNavItem(
        route = CreateJournalDestination,
        icon = Icons.Default.CollectionsBookmark,
        label = Res.string.bottom_nav_books_label,
    )

    object Settings : BottomNavItem(
        route = SettingDestination,
        icon = Icons.Default.AccountCircle,
        label = Res.string.bottom_nav_profile_label,
    )
}

val items = listOf(
    BottomNavItem.Home,
    BottomNavItem.CreateBook,
    BottomNavItem.Settings,
)

@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier, navigateTo: (Destination) -> Unit = {}) {
    var itemSelected by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }
    Column(modifier = modifier.fillMaxWidth().wrapContentHeight()) {
        HorizontalDivider(
            color = Color.LightGray.copy(alpha = 0.5f),
            thickness = 1.dp,
        )

        NavigationBar(
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = stringResource(item.label),
                            modifier = Modifier.size(30.dp),
                        )
                    },
                    label = { Text(text = stringResource(item.label)) },
                    selected = itemSelected == item,
                    colors = NavigationBarItemDefaults.colors().copy(
                        selectedIndicatorColor = Color.Transparent,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.tertiary,
                        unselectedTextColor = MaterialTheme.colorScheme.tertiary,
                    ),
                    onClick = {
                        itemSelected = item
                        navigateTo(item.route)
//                    navController.navigate(item.route) {
//                        popUpTo(navController.graph.startDestinationId)
//                        launchSingleTop = true
//                    }
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun BottomNavigationBarPreview() {
    LifetimeJournalTheme {
        BottomNavigationBar()
    }
}
