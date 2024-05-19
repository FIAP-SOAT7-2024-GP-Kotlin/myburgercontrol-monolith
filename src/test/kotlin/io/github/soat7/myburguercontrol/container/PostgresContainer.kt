package io.github.soat7.myburguercontrol.container

import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import java.util.UUID

class PostgresContainer : BeforeAllCallback {

    companion object {
        private const val DATABASE_NAME = "db_it"
        private const val DB_USERNAME = "it_user"
        private const val DB_PASSWORD = "123"
        private val container =
            PostgreSQLContainer(DockerImageName.parse("postgres:15-alpine"))
                .apply {
                    withCreateContainerCmdModifier { it.withName("pg_my_burger_control-${UUID.randomUUID()}") }
                    withDatabaseName(DATABASE_NAME)
                    withUsername(DB_USERNAME)
                    withPassword(DB_PASSWORD)
                    withExposedPorts(5432)
                }

        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    override fun beforeAll(context: ExtensionContext?) {
        if (!container.isRunning) {
            container.start()
            log.info("Postgres container started with host [${container.jdbcUrl}]")
            System.setProperty("spring.datasource.url", container.jdbcUrl)
            System.setProperty("spring.datasource.username", container.username)
            System.setProperty("spring.datasource.password", container.password)
        }
    }
}
