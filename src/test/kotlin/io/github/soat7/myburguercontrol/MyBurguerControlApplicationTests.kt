package io.github.soat7.myburguercontrol

import io.github.soat7.myburguercontrol.config.MyBurguerControlConfig
import io.github.soat7.myburguercontrol.config.PostgresContainerConfig
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.context.annotation.Import
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [MyBurguerControlTestConfig::class, MyBurguerControlApplication::class]
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class MyBurguerControlApplicationTests @Autowired constructor(
    private val restTemplate: TestRestTemplate
) {

    companion object {
        @JvmStatic
        private val postgresql = PostgresContainerConfig.postgresql

        @JvmStatic
        @DynamicPropertySource
        private fun configureSpringWithContainer(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresql::getJdbcUrl)
            registry.add("spring.datasource.username", postgresql::getUsername)
            registry.add("spring.datasource.password", postgresql::getPassword)
        }
    }

    @Test
    fun contextLoads() {
    }

    @Test
    fun `should call spring actuator to know if it is running ok`() {
        // Given

        // When
        val response = restTemplate.getForEntity<Map<String, String>>("/actuator/health")

        // Then
        assertTrue(response.statusCode.is2xxSuccessful)
        val body = response.body
        assertNotNull(body)
        assertEquals("status", body.keys.first())
        assertEquals("UP", body["status"])
    }
}

@AutoConfigureWebMvc
@Import(MyBurguerControlApplication::class, MyBurguerControlConfig::class, PostgresContainerConfig::class)
class MyBurguerControlTestConfig
