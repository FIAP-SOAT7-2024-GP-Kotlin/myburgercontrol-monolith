package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.application.ports.outbound.CustomerDatabasePort
import io.github.soat7.myburguercontrol.domain.exception.ReasonCodeException
import io.github.soat7.myburguercontrol.domain.model.Customer
import io.github.soat7.myburguercontrol.fixtures.CustomerFixtures
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpStatus
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
        val customer = CustomerFixtures.mockDomainCustomer(id = id, cpf = cpf)

        every { customerDatabasePort.findCustomerByCpf(any()) } returns null
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
    fun `should throw ReasonCodeException when Exception is thrown while finding customer by cpf`() {
        val cpf = "48024771802"

        every {
            customerDatabasePort.findCustomerByCpf(any())
        } throws Exception("Error while finding user by cpf")

        val response = assertThrows(ReasonCodeException::class.java) {
            service.findCustomerByCpf(cpf)
        }

        assertAll(
            Executable { assertThat(response is ReasonCodeException) },
            Executable { Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.reasonCode.status) }
        )

        verify(exactly = 1) { customerDatabasePort.findCustomerByCpf(any()) }
    }

    @Test
    @Order(3)
    fun `should throw ReasonCodeException when Exception is thrown while finding customer by id`() {
        val randomId = UUID.randomUUID()
        every {
            customerDatabasePort.findCustomerById(any())
        } throws Exception("Error while finding user by cpf")

        val response = assertThrows(ReasonCodeException::class.java) {
            service.findCustomerById(randomId)
        }

        assertAll(
            Executable { assertThat(response is ReasonCodeException) },
            Executable { Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.reasonCode.status) }
        )

        verify(exactly = 1) { customerDatabasePort.findCustomerById(any()) }
    }

    @Test
    @Order(4)
    fun `should throw ReasonCodeException when exception is thrown while creating a customer`() {
        val cpf = "48024771802"
        val id = UUID.randomUUID()
        val customer = CustomerFixtures.mockDomainCustomer(id = id, cpf = cpf)

        every { customerDatabasePort.findCustomerByCpf(any()) } returns null
        every {
            customerDatabasePort.create(any<Customer>())
        } throws Exception("Error while creating user")

        val response = assertThrows(ReasonCodeException::class.java) {
            service.create(customer)
        }

        assertAll(
            Executable { assertThat(response is ReasonCodeException) },
            Executable { Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.reasonCode.status) }
        )

        verify(exactly = 1) { customerDatabasePort.create(any<Customer>()) }
    }

    @Test
    @Order(4)
    fun `should throw ReasonCodeException when a customer with the same cpf is already registered`() {
        val cpf = "48024771802"
        val id = UUID.randomUUID()
        val customer = CustomerFixtures.mockDomainCustomer(id = id, cpf = cpf)

        every { customerDatabasePort.findCustomerByCpf(any()) } returns customer

        val response = assertThrows(ReasonCodeException::class.java) {
            service.create(customer)
        }

        assertAll(
            Executable { assertThat(response is ReasonCodeException) },
            Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.reasonCode.status) }
        )

        verify(exactly = 0) { customerDatabasePort.create(any()) }
    }

    @Test
    @Order(5)
    fun `should get a customer using CPF`() {
        val cpf = "48024771802"
        val id = UUID.randomUUID()
        val customer = CustomerFixtures.mockDomainCustomer(id = id, cpf = cpf)

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
