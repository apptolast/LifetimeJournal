package com.apptolast.familyfilmapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.apptolast.lifetimejournal.core.theme.BackgroundDark
import com.apptolast.lifetimejournal.core.theme.BackgroundLight
import com.apptolast.lifetimejournal.core.theme.ErrorContainerDark
import com.apptolast.lifetimejournal.core.theme.ErrorContainerLight
import com.apptolast.lifetimejournal.core.theme.ErrorDark
import com.apptolast.lifetimejournal.core.theme.ErrorLight
import com.apptolast.lifetimejournal.core.theme.GreenContainerDark
import com.apptolast.lifetimejournal.core.theme.GreenContainerLight
import com.apptolast.lifetimejournal.core.theme.GreenPrimaryDark
import com.apptolast.lifetimejournal.core.theme.GreenPrimaryLight
import com.apptolast.lifetimejournal.core.theme.GreenSecondaryContainerDark
import com.apptolast.lifetimejournal.core.theme.GreenSecondaryContainerLight
import com.apptolast.lifetimejournal.core.theme.GreenSecondaryDark
import com.apptolast.lifetimejournal.core.theme.GreenSecondaryLight
import com.apptolast.lifetimejournal.core.theme.GreenTertiaryContainerDark
import com.apptolast.lifetimejournal.core.theme.GreenTertiaryContainerLight
import com.apptolast.lifetimejournal.core.theme.GreenTertiaryDark
import com.apptolast.lifetimejournal.core.theme.GreenTertiaryLight
import com.apptolast.lifetimejournal.core.theme.InterTypography
import com.apptolast.lifetimejournal.core.theme.OnBackgroundDark
import com.apptolast.lifetimejournal.core.theme.OnBackgroundLight
import com.apptolast.lifetimejournal.core.theme.OnErrorContainerDark
import com.apptolast.lifetimejournal.core.theme.OnErrorContainerLight
import com.apptolast.lifetimejournal.core.theme.OnErrorDark
import com.apptolast.lifetimejournal.core.theme.OnErrorLight
import com.apptolast.lifetimejournal.core.theme.OnGreenContainerDark
import com.apptolast.lifetimejournal.core.theme.OnGreenContainerLight
import com.apptolast.lifetimejournal.core.theme.OnGreenDark
import com.apptolast.lifetimejournal.core.theme.OnGreenLight
import com.apptolast.lifetimejournal.core.theme.OnGreenSecondaryContainerDark
import com.apptolast.lifetimejournal.core.theme.OnGreenSecondaryContainerLight
import com.apptolast.lifetimejournal.core.theme.OnGreenSecondaryDark
import com.apptolast.lifetimejournal.core.theme.OnGreenSecondaryLight
import com.apptolast.lifetimejournal.core.theme.OnGreenTertiaryContainerDark
import com.apptolast.lifetimejournal.core.theme.OnGreenTertiaryContainerLight
import com.apptolast.lifetimejournal.core.theme.OnGreenTertiaryDark
import com.apptolast.lifetimejournal.core.theme.OnGreenTertiaryLight
import com.apptolast.lifetimejournal.core.theme.OnSurfaceDark
import com.apptolast.lifetimejournal.core.theme.OnSurfaceLight
import com.apptolast.lifetimejournal.core.theme.OnSurfaceVariantDark
import com.apptolast.lifetimejournal.core.theme.OnSurfaceVariantLight
import com.apptolast.lifetimejournal.core.theme.OutlineDark
import com.apptolast.lifetimejournal.core.theme.OutlineLight
import com.apptolast.lifetimejournal.core.theme.SurfaceDark
import com.apptolast.lifetimejournal.core.theme.SurfaceLight
import com.apptolast.lifetimejournal.core.theme.SurfaceVariantDark
import com.apptolast.lifetimejournal.core.theme.SurfaceVariantLight

val DarkColorScheme = darkColorScheme(
    primary = GreenPrimaryDark,
    secondary = GreenSecondaryDark,
    tertiary = GreenTertiaryDark,
    onPrimary = OnGreenDark,
    primaryContainer = GreenContainerDark,
    onPrimaryContainer = OnGreenContainerDark,
    onSecondary = OnGreenSecondaryDark,
    secondaryContainer = GreenSecondaryContainerDark,
    onSecondaryContainer = OnGreenSecondaryContainerDark,
    onTertiary = OnGreenTertiaryDark,
    onTertiaryContainer = OnGreenTertiaryContainerDark,
    tertiaryContainer = GreenTertiaryContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    outline = OutlineDark,
)

val LightColorScheme = lightColorScheme(
    primary = GreenPrimaryLight,
    secondary = GreenSecondaryLight,
    tertiary = GreenTertiaryLight,
    onPrimary = OnGreenLight,
    primaryContainer = GreenContainerLight,
    onPrimaryContainer = OnGreenContainerLight,
    onSecondary = OnGreenSecondaryLight,
    secondaryContainer = GreenSecondaryContainerLight,
    onSecondaryContainer = OnGreenSecondaryContainerLight,
    onTertiary = OnGreenTertiaryLight,
    onTertiaryContainer = OnGreenTertiaryContainerLight,
    tertiaryContainer = GreenTertiaryContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    outline = OutlineLight,
)

@Composable
fun FamilyFilmAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = InterTypography(),
        content = content,
    )
}
