package io.github.soat7.myburguercontrol.service

import io.github.soat7.myburguercontrol.application.ports.outbound.CustomerDatabasePort
import io.github.soat7.myburguercontrol.domain.model.Customer
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
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CustomerServiceTest {

    private val customerDatabasePort = mockk<CustomerDatabasePort>()
    private val service = CustomerService(customerDatabasePort)

    @BeforeTest
    fun setUp() {
        clearMocks(customerDatabasePort)
    }

    @Test
    @Order(1)
    fun `should create a customer using CPF`() {
        val cpf = "48024771802"
        val id = UUID.randomUUID()
        val customer = CustomerFixtures.mockCustomer(id = id, cpf = cpf)

        every {
            customerDatabasePort.create(any<Customer>())
        } returns customer

        val response = assertDoesNotThrow {
            service.create(customer)
        }

        assertEquals(cpf, response.cpf)
        assertEquals(id, response.id)

        verify(exactly = 1) { customerDatabasePort.create(any<Customer>()) }
    }

    @Test
    @Order(2)
    fun `should get a customer using CPF`() {
        val cpf = "48024771802"
        val id = UUID.randomUUID()
        val customer = CustomerFixtures.mockCustomer(id = id, cpf = cpf)

        every { customerDatabasePort.findCustomerById(any()) } returns customer

        val response = assertDoesNotThrow {
            service.findCustomerById(id)
        }

        assertNotNull(response)
        assertEquals(id, response.id)
        assertEquals(cpf, response.cpf)

        verify(exactly = 1) { customerDatabasePort.findCustomerById(any()) }
    }
}
