package com.apptolast.lifetimejournal

import com.apptolast.lifetimejournal.modules.configureDatabases
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    //configureDatabases()
    routing {
        get("/") {
            call.respondText("Ktor hello world!")
        }
    }
}
