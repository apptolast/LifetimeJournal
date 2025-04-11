package com.apptolast.lifetimejournal

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform