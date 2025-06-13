package com.apptolast.lifetimejournal.modules

import io.ktor.server.application.Application
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI


fun Application.configureDatabases() {
    val uri = RedisURI.Builder
        .redis("138.199.157.58", 32079)
        .build()

    val client = RedisClient.create(uri)
    val connection = client.connect()
    val commands = connection.sync()

    commands["foo"] = "bar"
    val result = commands["foo"]
    println(result) // >>> bar

    connection.close()

    client.shutdown()
}
