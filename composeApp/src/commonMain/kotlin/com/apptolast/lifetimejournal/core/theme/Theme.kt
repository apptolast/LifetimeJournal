package com.apptolast.lifetimejournal.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = RosePrimary,
    onPrimary = RoseOnPrimary,
    primaryContainer = RosePrimaryContainer,
    onPrimaryContainer = RoseOnPrimaryContainer,
    secondary = RoseSecondary,
    onSecondary = RoseOnSecondary,
    secondaryContainer = RoseSecondaryContainer,
    onSecondaryContainer = RoseOnSecondaryContainer,
    tertiary = RoseTertiary,
    onTertiary = RoseOnTertiary,
    tertiaryContainer = RoseTertiaryContainer,
    onTertiaryContainer = RoseOnTertiaryContainer,
    error = RoseError,
    onError = RoseOnError,
    errorContainer = RoseErrorContainer,
    onErrorContainer = RoseOnErrorContainer,
    background = RoseBackground,
    onBackground = RoseOnBackground,
    surface = RoseSurface,
    onSurface = RoseOnSurface,
    surfaceVariant = RoseSurfaceVariant,
    onSurfaceVariant = RoseOnSurfaceVariant,
    outline = RoseOutline,
    outlineVariant = RoseOutlineVariant,
    surfaceContainerLowest = RoseSurfaceContainerLowest,
    surfaceContainerLow = RoseSurfaceContainerLow,
    surfaceContainer = RoseSurfaceContainer,
    surfaceContainerHigh = RoseSurfaceContainerHigh,
    surfaceContainerHighest = RoseSurfaceContainerHighest,
)

private val DarkColorScheme = darkColorScheme(
    primary = RosePrimaryDark,
    onPrimary = RoseOnPrimaryDark,
    primaryContainer = RosePrimaryContainerDark,
    onPrimaryContainer = RoseOnPrimaryContainerDark,
    secondary = RoseSecondaryDark,
    onSecondary = RoseOnSecondaryDark,
    secondaryContainer = RoseSecondaryContainerDark,
    onSecondaryContainer = RoseOnSecondaryContainerDark,
    tertiary = RoseTertiaryDark,
    onTertiary = RoseOnTertiaryDark,
    tertiaryContainer = RoseTertiaryContainerDark,
    onTertiaryContainer = RoseOnTertiaryContainerDark,
    error = RoseErrorDark,
    onError = RoseOnErrorDark,
    errorContainer = RoseErrorContainerDark,
    onErrorContainer = RoseOnErrorContainerDark,
    background = RoseBackgroundDark,
    onBackground = RoseOnBackgroundDark,
    surface = RoseSurfaceDark,
    onSurface = RoseOnSurfaceDark,
    surfaceVariant = RoseSurfaceVariantDark,
    onSurfaceVariant = RoseOnSurfaceVariantDark,
    outline = RoseOutlineDark,
    outlineVariant = RoseOutlineVariantDark,
    surfaceContainerLowest = RoseSurfaceContainerLowestDark,
    surfaceContainerLow = RoseSurfaceContainerLowDark,
    surfaceContainer = RoseSurfaceContainerDark,
    surfaceContainerHigh = RoseSurfaceContainerHighDark,
    surfaceContainerHighest = RoseSurfaceContainerHighestDark,
)

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp),
)

@Composable
fun LifetimeJournalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = PoppinsTypography(),
        shapes = AppShapes,
        content = content,
    )
}
