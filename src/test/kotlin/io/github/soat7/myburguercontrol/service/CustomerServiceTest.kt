package io.github.soat7.myburguercontrol.service

import io.github.soat7.myburguercontrol.domain.entity.CustomerEntity
import io.github.soat7.myburguercontrol.domain.ports.CustomerDatabasePort
import io.github.soat7.myburguercontrol.domain.service.CustomerService
import io.github.soat7.myburguercontrol.fixtures.CustomerFixtures
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.data.repository.findByIdOrNull
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CustomerServiceTest {

    private val repository = mockk<CustomerDatabasePort>()
    private val service = CustomerService(repository)

    @BeforeTest
    fun setUp() {
        clearMocks(repository)
    }

    @Test
    @Order(1)
    fun `should create a customer using CPF`() {
        val cpf = "48024771802"
        val id = UUID.randomUUID()
        val customer = CustomerFixtures.mockCustomerEntity(id = id, cpf = cpf)

        every {
            repository.save(any<CustomerEntity>())
        } returns customer

        val response = assertDoesNotThrow {
            service.create(customer)
        }

        assertEquals(cpf, response.cpf)
        assertEquals(id, response.id)

        verify(exactly = 1) { repository.save(any<CustomerEntity>()) }
    }

    @Test
    @Order(2)
    fun `should get a customer using CPF`() {
        val cpf = "48024771802"
        val id = UUID.randomUUID()
        val customer = CustomerFixtures.mockCustomerEntity(id = id, cpf = cpf)

        every { repository.findByIdOrNull(any()) } returns customer

        val response = assertDoesNotThrow {
            service.findCustomerById(id)
        }

        assertNotNull(response)
        assertEquals(id, response.id)
        assertEquals(cpf, response.cpf)

        verify(exactly = 1) { repository.findById(any()) }
    }
}
