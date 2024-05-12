package io.github.soat7.myburguercontrol.infrastructure.rest

import io.github.soat7.myburguercontrol.base.BaseIntegrationTest
import io.github.soat7.myburguercontrol.fixtures.CustomerFixtures
import io.github.soat7.myburguercontrol.infrastructure.persistence.customer.repository.CustomerRepository
import io.github.soat7.myburguercontrol.infrastructure.rest.customer.api.response.CustomerResponse
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import java.util.UUID

class CustomerIT : BaseIntegrationTest() {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Test
    fun `should successfully create a new customer`() {
        val cpf = "48024771802"
        val inputCustomerData = CustomerFixtures.mockCustomerCreationRequest(cpf)

        val response = restTemplate.postForEntity<CustomerResponse>("/api/v1/customers", inputCustomerData)

        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertNotNull(response.body) },
            Executable { assertEquals(cpf, response.body!!.cpf) }
        )
        val savedCustomer = customerRepository.findByIdOrNull(response.body!!.id)

        assertAll(
            Executable { assertNotNull(savedCustomer) },
            Executable { assertEquals(cpf, savedCustomer!!.cpf) },
            Executable { assertEquals(response.body!!.id, savedCustomer!!.id) }
        )
    }

    @Test
    fun `should successfully find a customer by id`() {
        val cpf = "45661450001"
        val customer = customerRepository.save(CustomerFixtures.mockCustomerEntity(cpf = cpf))

        val response = restTemplate.getForEntity<CustomerResponse>(
            url = "/api/v1/customers/{id}",
            uriVariables = mapOf(
                "id" to customer.id
            )
        )

        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertNotNull(response.body) },
            Executable { assertEquals(customer.id, response.body!!.id) },
            Executable { assertEquals(cpf, response.body!!.cpf) }
        )
    }

    @Test
    fun `should return NOT_FOUND when no customer is found for the given Id`() {
        val randomId = UUID.randomUUID()

        val response = restTemplate.getForEntity<CustomerResponse>(
            url = "/api/v1/customers/{id}",
            uriVariables = mapOf(
                "id" to randomId.toString()
            )
        )
        assertEquals(response.statusCode.value(), HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `should successfully find a customer by cpf`() {
        val cpf = "79569068060"

        val customer = customerRepository.save(CustomerFixtures.mockCustomerEntity(cpf = cpf))

        val response = restTemplate.getForEntity<CustomerResponse>(
            url = "/api/v1/customers?cpf={cpf}",
            uriVariables = mapOf(
                "cpf" to cpf
            )
        )

        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertNotNull(response.body) },
            Executable { assertEquals(customer.id, response.body!!.id) },
            Executable { assertEquals(cpf, response.body!!.cpf) }
        )
    }

    @Test
    fun `should return NOT_FOUND when no customer is found for the given`() {
        val cpf = "10974990060"

        val response = restTemplate.getForEntity<CustomerResponse>(
            url = "/api/v1/customers?cpf={cpf}",
            uriVariables = mapOf(
                "cpf" to cpf
            )
        )
        assertEquals(response.statusCode.value(), HttpStatus.NOT_FOUND.value())
    }
}
