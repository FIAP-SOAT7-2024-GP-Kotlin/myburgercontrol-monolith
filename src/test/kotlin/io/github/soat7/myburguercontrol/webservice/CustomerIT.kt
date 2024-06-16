package io.github.soat7.myburguercontrol.webservice

import io.github.soat7.myburguercontrol.base.BaseIntegrationTest
import io.github.soat7.myburguercontrol.fixtures.CustomerFixtures
import io.github.soat7.myburguercontrol.webservice.customer.api.response.CustomerResponse
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.boot.test.web.client.exchange
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.util.UUID

class CustomerIT : BaseIntegrationTest() {

    @Test
    fun `should successfully create a new customer`() {
        val cpf = "58737317059"
        val inputCustomerData = CustomerFixtures.mockCustomerCreationRequest(cpf)

        val response = restTemplate.exchange<CustomerResponse>(
            url = "/customers",
            method = HttpMethod.POST,
            requestEntity = HttpEntity(inputCustomerData, authenticationHeader),
        )

        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertNotNull(response.body) },
            Executable { assertEquals(cpf, response.body!!.cpf) },
        )
        val savedCustomer = customerJpaRepository.findByIdOrNull(response.body!!.id)

        assertAll(
            Executable { assertNotNull(savedCustomer) },
            Executable { assertEquals(cpf, savedCustomer!!.cpf) },
            Executable { assertEquals(response.body!!.id, savedCustomer!!.id) },
        )
    }

    @Test
    fun `should return BAD_REQUEST when trying to create a new customer with a cpf that is already registered`() {
        val cpf = "82709425025"
        val inputCustomerData = CustomerFixtures.mockCustomerCreationRequest(cpf)
        customerJpaRepository.save(CustomerFixtures.mockCustomerEntity(UUID.randomUUID(), cpf))

        val response = restTemplate.exchange<Any>(
            url = "/customers",
            method = HttpMethod.POST,
            requestEntity = HttpEntity(inputCustomerData, authenticationHeader),
        )

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun `should successfully find a customer by id`() {
        val cpf = "45661450001"
        val customer = insertCustomerData(CustomerFixtures.mockDomainCustomer(cpf = cpf))

        val response = restTemplate.exchange<CustomerResponse>(
            url = "/customers/{id}",
            method = HttpMethod.GET,
            requestEntity = HttpEntity(null, authenticationHeader),
            uriVariables = mapOf(
                "id" to customer.id,
            ),
        )

        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertNotNull(response.body) },
            Executable { assertEquals(customer.id, response.body!!.id) },
            Executable { assertEquals(cpf, response.body!!.cpf) },
        )
    }

    @Test
    fun `should return NOT_FOUND when no customer is found for the given Id`() {
        val randomId = UUID.randomUUID()

        val response = restTemplate.exchange<CustomerResponse>(
            url = "/customers/{id}",
            method = HttpMethod.GET,
            requestEntity = HttpEntity(null, authenticationHeader),
            uriVariables = mapOf(
                "id" to randomId.toString(),
            ),
        )
        assertEquals(response.statusCode.value(), HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `should successfully find a customer by cpf`() {
        val cpf = "79569068060"

        val customer = insertCustomerData(CustomerFixtures.mockDomainCustomer(cpf = cpf))

        val response = restTemplate.exchange<CustomerResponse>(
            url = "/customers?cpf={cpf}",
            method = HttpMethod.GET,
            requestEntity = HttpEntity(null, authenticationHeader),
            uriVariables = mapOf(
                "cpf" to cpf,
            ),
        )

        assertAll(
            Executable { assertTrue(response.statusCode.is2xxSuccessful) },
            Executable { assertNotNull(response.body) },
            Executable { assertEquals(customer.id, response.body!!.id) },
            Executable { assertEquals(cpf, response.body!!.cpf) },
        )
    }

    @Test
    fun `should return NOT_FOUND when no customer is found for the given`() {
        val cpf = "10974990060"

        val response = restTemplate.exchange<CustomerResponse>(
            url = "/customers?cpf={cpf}",
            method = HttpMethod.GET,
            requestEntity = HttpEntity(null, authenticationHeader),
            uriVariables = mapOf(
                "cpf" to cpf,
            ),
        )
        assertEquals(response.statusCode.value(), HttpStatus.NOT_FOUND.value())
    }
}
