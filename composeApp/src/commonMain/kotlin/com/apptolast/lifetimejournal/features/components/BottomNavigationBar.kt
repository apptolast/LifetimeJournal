package com.apptolast.lifetimejournal.features.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.apptolast.lifetimejournal.core.navigation.Destination
import com.apptolast.lifetimejournal.core.navigation.JournalDestination
import com.apptolast.lifetimejournal.core.navigation.SettingDestination
import com.apptolast.lifetimejournal.core.navigation.StoryBooksListDestination
import com.apptolast.lifetimejournal.core.theme.LifetimeJournalTheme
import com.apptolast.lifetimejournal.resources.Res
import com.apptolast.lifetimejournal.resources.bottom_nav_books_label
import com.apptolast.lifetimejournal.resources.bottom_nav_home_label
import com.apptolast.lifetimejournal.resources.bottom_nav_profile_label
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

sealed class BottomNavItem(val route: Destination, val icon: ImageVector, val label: StringResource) {
    object Diaries : BottomNavItem(
        route = JournalDestination,
        icon = Icons.Default.Book,
        label = Res.string.bottom_nav_home_label,
    )

    object StoryBooks : BottomNavItem(
        route = StoryBooksListDestination,
        icon = Icons.AutoMirrored.Filled.MenuBook,
        label = Res.string.bottom_nav_books_label,
    )

    object Profile : BottomNavItem(
        route = SettingDestination,
        icon = Icons.Default.AccountCircle,
        label = Res.string.bottom_nav_profile_label,
    )
}

val items = listOf(
    BottomNavItem.Diaries,
    BottomNavItem.StoryBooks,
    BottomNavItem.Profile,
)

@Composable
fun BottomNavigationBar(
    currentRoute: Destination,
    modifier: Modifier = Modifier,
    navigateTo: (Destination) -> Unit = {},
) {
    Column(modifier = modifier.fillMaxWidth().wrapContentHeight()) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp,
        )

        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = stringResource(item.label),
                            modifier = Modifier.size(28.dp),
                        )
                    },
                    label = { Text(text = stringResource(item.label)) },
                    selected = currentRoute == item.route,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                    onClick = {
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
        BottomNavigationBar(currentRoute = JournalDestination)
    }
}
