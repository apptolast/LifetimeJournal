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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.apptolast.lifetimejournal.core.navigation.Destination
import com.apptolast.lifetimejournal.core.navigation.JournalDestination
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
        route = JournalDestination,
        icon = Icons.Default.Home,
        label = Res.string.bottom_nav_home_label,
    )

    object Books : BottomNavItem(
        route = JournalDestination,
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
    BottomNavItem.Books,
    BottomNavItem.Settings,
)

@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier, navigateTo: (Destination) -> Unit = {}) {
    var itemSelected by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }
    Column(modifier = modifier.fillMaxWidth().wrapContentHeight()) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp,
        )

        NavigationBar(
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.surface,
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
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                    onClick = {
                        itemSelected = item
                        navigateTo(item.route)
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
