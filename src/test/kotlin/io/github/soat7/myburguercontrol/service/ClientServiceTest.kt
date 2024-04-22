package io.github.soat7.myburguercontrol.service

import io.github.soat7.myburguercontrol.dto.ClientDTO
import io.github.soat7.myburguercontrol.entities.ClientEntity
import io.github.soat7.myburguercontrol.repository.ClientRepository
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ClientServiceTest {

    private val repository = mockk<ClientRepository>()
    private val service = ClientService(repository)

    @BeforeTest
    fun setUp() {
        clearMocks(repository)
    }

    @Test
    @Order(1)
    fun `should create a client using CPF`() {
        // Given
        val cpf = "48024771802"

        every {
            repository.save(any<ClientEntity>())
        } returns ClientEntity(
            id = (1..1000).random().toLong(),
            cpf = cpf
        )

        val inputClientData = ClientDTO(
            cpf = cpf
        )

        // When
        val response = service.create(inputClientData)

        // Then
        verify(exactly = 1) { repository.save(any<ClientEntity>()) }

        assertEquals(cpf, response.cpf)
        assertNotEquals(0, response.id)
    }

    @Test
    @Order(2)
    fun `should get a client using CPF`() {
        // Given
        val cpf = "48024771802"
        val id = 1L

        every { repository.findByIdOrNull(any<Long>()) } returns ClientEntity(
            id = id,
            cpf = cpf
        )

        // When
        val response = service.getClient(id)

        // Then
        verify(exactly = 1) { repository.findById(any<Long>()) }

        assertNotNull(response)
        assertEquals(id, response.id)
        assertEquals(cpf, response.cpf)
    }
}