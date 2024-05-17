package io.github.soat7.myburguercontrol.infrastructure.rest

import io.github.soat7.myburguercontrol.base.BaseIntegrationTest
import io.github.soat7.myburguercontrol.domain.enum.OrderStatus
import io.github.soat7.myburguercontrol.fixtures.CustomerFixtures.mockCustomerEntity
import io.github.soat7.myburguercontrol.infrastructure.persistence.customer.repository.CustomerRepository
import io.github.soat7.myburguercontrol.infrastructure.persistence.order.repository.OrderRepository
import io.github.soat7.myburguercontrol.infrastructure.rest.customer.api.request.OrderCreationRequest
import io.github.soat7.myburguercontrol.infrastructure.rest.customer.api.response.OrderResponse
import io.github.soat7.myburguercontrol.util.toBigDecimal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import kotlin.jvm.optionals.getOrNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class OrderIT : BaseIntegrationTest() {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Test
    fun `should create a new order using cpf`() {
        // Given
        val cpf = "23282711034"
        val customer = customerRepository.save(mockCustomerEntity(cpf = cpf))
        val inputOrderData = OrderCreationRequest(cpf = customer.cpf)

        // When
        val orderResponse = restTemplate.postForEntity<OrderResponse>("/orders", inputOrderData).body

        // Then
        assertNotNull(orderResponse)

        val order = orderRepository.findById(orderResponse.id).getOrNull()

        assertNotNull(order)

        assertEquals(cpf, order.customer.cpf)
        assertEquals(OrderStatus.NEW.name, order.status)
        assertTrue(order.items.isEmpty())
    }

    @Test
    fun `should get orders using cpf`() {
        // Given
        val cpf = "23282711034"

        // When
        val orders = restTemplate.getForEntity<List<OrderResponse>>(
            "/orders?cpf={cpf}", uriVariables = mapOf(
                "cpf" to cpf
            )
        ).body!!

        // Then
        assertFalse(orders.isEmpty())

        val order = orders.first()

        assertNotNull(order.id)
        assertEquals(cpf, order.customer.cpf)
        assertEquals(OrderStatus.NEW, order.status)
        assertTrue(order.items.isEmpty())
        assertEquals(0.0.toBigDecimal(), order.total)
    }
}
