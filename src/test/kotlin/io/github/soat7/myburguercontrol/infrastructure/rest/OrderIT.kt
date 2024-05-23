package io.github.soat7.myburguercontrol.infrastructure.rest

import io.github.soat7.myburguercontrol.base.BaseIntegrationTest
import io.github.soat7.myburguercontrol.domain.enum.OrderStatus
import io.github.soat7.myburguercontrol.fixtures.CustomerFixtures.mockDomainCustomer
import io.github.soat7.myburguercontrol.infrastructure.rest.order.api.request.OrderCreationRequest
import io.github.soat7.myburguercontrol.infrastructure.rest.order.api.response.OrderResponse
import io.github.soat7.myburguercontrol.util.toBigDecimal
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.function.Executable
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import kotlin.jvm.optionals.getOrNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class OrderIT : BaseIntegrationTest() {

    @Test
    fun `should create a new order using cpf`() {
        val cpf = "23282711034"
        val customer = insertCustomerData(mockDomainCustomer(cpf = cpf))
        val items = insertProducts().map {
            OrderCreationRequest.OrderItem(
                productId = it,
                quantity = 1
            )
        }

        val inputOrderData = OrderCreationRequest(customerCpf = customer.cpf, items)

        val orderResponse = restTemplate.postForEntity<OrderResponse>(
            "/orders",
            inputOrderData
        )

        assertAll(
            Executable { assertTrue(orderResponse.statusCode.is2xxSuccessful) },
            Executable { assertNotNull(orderResponse.body) }
        )

        val order = orderRepository.findById(orderResponse.body!!.id).getOrNull()

        assertAll(
            Executable { assertNotNull(order) },
            Executable { assertEquals(cpf, order!!.customer.cpf) },
            Executable { assertEquals(OrderStatus.NEW.name, order!!.status) },
            Executable { assertTrue(order!!.items.isEmpty()) }
        )
    }

    @Test
    fun `should get orders using cpf`() {
        val cpf = "23282711034"

        val orders = restTemplate.getForEntity<List<OrderResponse>>(
            "/orders?cpf={cpf}",
            uriVariables = mapOf(
                "cpf" to cpf
            )
        )

        assertAll(
            Executable { assertNotNull(orders.body) },
            Executable { assertFalse(orders.body!!.isEmpty()) }
        )

        val order = orders.body!!.first()
        assertAll(
            Executable { assertNotNull(order.id) },
            Executable { assertEquals(cpf, order.customer.cpf) },
            Executable { assertEquals(OrderStatus.NEW, order.status) },
            Executable { assertTrue(order.items.isEmpty()) },
            Executable { assertEquals(0.0.toBigDecimal(), order.total) }
        )
    }

    @Test
    fun `should return BAD_REQUEST when trying to create an order for a customer that was not found`() {
        val cpf = "44527073001"
        val items = insertProducts().map {
            OrderCreationRequest.OrderItem(
                productId = it,
                quantity = 1
            )
        }
        val inputOrderData = OrderCreationRequest(customerCpf = cpf, items)

        val response = restTemplate.postForEntity<OrderResponse>(
            url = "/orders",
            inputOrderData
        )

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }
}
