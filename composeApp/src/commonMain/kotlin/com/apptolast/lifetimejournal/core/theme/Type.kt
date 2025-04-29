package com.apptolast.lifetimejournal.core.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.apptolast.lifetimejournal.resources.Res
import com.apptolast.lifetimejournal.resources.poppins_bold
import com.apptolast.lifetimejournal.resources.poppins_medium
import com.apptolast.lifetimejournal.resources.poppins_regular
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PoppinsFontFamily() = FontFamily(
    Font(Res.font.poppins_regular, weight = FontWeight.Light),
    Font(Res.font.poppins_bold, weight = FontWeight.Normal),
    Font(Res.font.poppins_medium, weight = FontWeight.Medium),
)

@Composable
fun PoppinsTypography() = Typography().run {
    val fontFamily = PoppinsFontFamily()
    this.copy(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = titleLarge.copy(fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        labelLarge = labelLarge.copy(fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontFamily = fontFamily),
    )
}

@Preview()
@Composable
private fun TypographyPreview() {
    LifetimeJournalTheme {
        Column(modifier = Modifier.background(color = Color.White).padding(4.dp)) {
            Text(text = "displayLarge", style = MaterialTheme.typography.displayLarge)
            Text(text = "displayLarge", style = MaterialTheme.typography.displayLarge)
            Text(text = "displayMedium", style = MaterialTheme.typography.displayMedium)
            Text(text = "displaySmall", style = MaterialTheme.typography.displaySmall)
            Text(text = "headlineLarge", style = MaterialTheme.typography.headlineLarge)
            Text(text = "headlineMedium", style = MaterialTheme.typography.headlineMedium)
            Text(text = "headlineSmall", style = MaterialTheme.typography.headlineSmall)
            Text(text = "titleLarge", style = MaterialTheme.typography.titleLarge)
            Text(text = "titleMedium", style = MaterialTheme.typography.titleMedium)
            Text(text = "titleSmall", style = MaterialTheme.typography.titleSmall)
            Text(text = "bodyLarge", style = MaterialTheme.typography.bodyLarge)
            Text(text = "bodyMedium", style = MaterialTheme.typography.bodyMedium)
            Text(text = "bodySmall", style = MaterialTheme.typography.bodySmall)
            Text(text = "labelLarge", style = MaterialTheme.typography.labelLarge)
            Text(text = "labelMedium", style = MaterialTheme.typography.labelMedium)
            Text(text = "labelSmall", style = MaterialTheme.typography.labelSmall)
        }
    }
}
