package io.github.soat7.myburguercontrol.config

import org.testcontainers.containers.PostgreSQLContainer

object PostgresContainerConfig {

    @JvmStatic
    val postgresql = PostgreSQLContainer("postgres:16-alpine").apply {
        withDatabaseName("myburguer")
        withUsername(System.getenv("DATABASE_USER"))
        withPassword(System.getenv("DATABASE_PASSWORD"))
        withEnv(mapOf("PGDATA" to "/var/lib/postgresql/data"))
        withTmpFs(mapOf("/var/lib/postgresql/data" to "rw"))
        withReuse(true)
        withUrlParam("TC_REUSABLE", "true")
        start()
    }

    fun waitUntilUp() {
        do {
            Thread.sleep(100)
        } while (!postgresql.isCreated())
    }

    fun stop() {
        postgresql.stop()
    }
}
