package com.apptolast.lifetimejournal.previews

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.apptolast.lifetimejournal.features.home.data.HomeState
import com.apptolast.lifetimejournal.features.home.presentation.HomeScreen
import com.sunildhiman90.kmauth.core.KMAuthUser

@OptIn(ExperimentalCoilApi::class)
@Preview(showBackground = true)
@Preview(showBackground = true, locale = "es")
@Composable
fun HomeContentPreview(modifier: Modifier = Modifier) {
    MaterialTheme {
        val previewHandler = AsyncImagePreviewHandler {
            ColorImage(Color.Red.toArgb())
        }

        CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
            HomeScreen(
                state = HomeState().copy(isLoading = false),
                user = KMAuthUser(id = "").copy(
                    name = "John Doe",
                    profilePicUrl =
                    "https://fastly.picsum.photos/id/237/200/300.jpg?hmac=TmmQSbShHz9CdQm0NkEjx1Dyh_Y984R9LpNrpvH2D_U",
                ),
            )
        }
    }
}
