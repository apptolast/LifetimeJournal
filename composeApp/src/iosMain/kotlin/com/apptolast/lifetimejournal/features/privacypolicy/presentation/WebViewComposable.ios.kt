package com.apptolast.lifetimejournal.features.privacypolicy.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKWebView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun WebViewComposable(url: String, modifier: Modifier) {
    UIKitView(
        factory = {
            WKWebView().apply {
                val nsUrl = NSURL.URLWithString(url)
                if (nsUrl != null) {
                    loadRequest(NSURLRequest.requestWithURL(nsUrl))
                }
            }
        },
        modifier = modifier,
    )
}
