package io.github.soat7.myburguercontrol.controller

import io.github.soat7.myburguercontrol.MyBurguerControlTestConfig
import io.github.soat7.myburguercontrol.config.PostgresContainerConfig
import io.github.soat7.myburguercontrol.dto.ClientDTO
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import kotlin.test.*

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [MyBurguerControlTestConfig::class]
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ClientControllerTest @Autowired constructor(
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
    @Order(1)
    fun `should create a client using CPF`() {
        // Given
        val cpf = "48024771802"
        val inputClientData = ClientDTO(
            cpf = cpf
        )

        // When
        val response = restTemplate.postForEntity<ClientDTO>("/client", inputClientData)

        // Then
        assertTrue(response.statusCode.is2xxSuccessful)
        val body = response.body
        assertNotNull(body)
        assertEquals(cpf, body.cpf)
        assertNotEquals(0, body.id)
    }

    @Test
    @Order(2)
    fun `should get a client using CPF`() {
        // Given
        val cpf = "48024771802"
        val id = 1L

        // When
        val response = restTemplate.getForEntity<ClientDTO>(
            url = "/client/{id}",
            uriVariables = mapOf(
                "id" to id
            )
        )

        // Then
        assertTrue(response.statusCode.is2xxSuccessful)
        val body = response.body
        assertNotNull(body)
        assertEquals(id, body.id)
        assertEquals(cpf, body.cpf)
    }
}