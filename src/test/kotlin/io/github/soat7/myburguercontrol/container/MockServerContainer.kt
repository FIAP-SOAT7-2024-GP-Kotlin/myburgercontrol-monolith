package io.github.soat7.myburguercontrol.container

import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.mockserver.client.MockServerClient
import org.testcontainers.containers.MockServerContainer
import org.testcontainers.utility.DockerImageName
import java.util.UUID

class MockServerContainer : BeforeAllCallback {

    companion object {
        private const val PORT = 1080
        private const val CONTAINER_NAME = "mock-server-test"
        private const val VERSION = "5.13.2"

        val container: MockServerContainer = MockServerContainer(
            DockerImageName.parse("mockserver/mockserver:mockserver-$VERSION"),
        )

        lateinit var client: MockServerClient
            private set
    }

    override fun beforeAll(context: ExtensionContext?) {
        if (!container.isRunning) {
            val containerNameSuffix: String = UUID.randomUUID().toString().replace("-", "").substring(0, 8)
            container
                .withCreateContainerCmdModifier { it.withName("$CONTAINER_NAME-$containerNameSuffix") }
                .start()

            System.setProperty(
                "mock-server.url",
                String.format("http://%s:%d", container.host, container.getMappedPort(PORT)),
            )
        }

        client = MockServerClient(container.host, container.getMappedPort(PORT))
    }
}
