package io.github.soat7.myburguercontrol.webservice

import io.github.soat7.myburguercontrol.base.BaseIntegrationTest
import io.github.soat7.myburguercontrol.business.enum.OrderStatus
import io.github.soat7.myburguercontrol.fixtures.CustomerFixtures.mockDomainCustomer
import io.github.soat7.myburguercontrol.fixtures.HttpMockRequest
import io.github.soat7.myburguercontrol.fixtures.OrderFixtures
import io.github.soat7.myburguercontrol.fixtures.PaymentFixtures.mockPayment
import io.github.soat7.myburguercontrol.util.toBigDecimal
import io.github.soat7.myburguercontrol.webservice.order.api.request.OrderCreationRequest
import io.github.soat7.myburguercontrol.webservice.order.api.response.OrderResponse
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.function.Executable
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import kotlin.jvm.optionals.getOrNull
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderIT : BaseIntegrationTest() {

    @Test
    fun `should create a new order`() {
        val cpf = "23282711034"
        val customer = insertCustomerData(mockDomainCustomer(cpf = cpf))
        val items = insertProducts().map {
            OrderCreationRequest.OrderItem(
                productId = it.id!!,
                quantity = 1,
            )
        }

        HttpMockRequest.mockApprovedPayment()
        val inputOrderData = OrderCreationRequest(customerCpf = customer.cpf, items)

        val orderResponse = restTemplate.exchange<OrderResponse>(
            url = "/orders",
            method = HttpMethod.POST,
            requestEntity = HttpEntity(inputOrderData, authenticationHeader),
        )

        assertAll(
            Executable { assertTrue(orderResponse.statusCode.is2xxSuccessful) },
            Executable { assertNotNull(orderResponse.body) },
        )

        val order = orderJpaRepository.findById(orderResponse.body!!.id).getOrNull()

        assertAll(
            Executable { assertNotNull(order) },
            Executable { assertEquals(cpf, order!!.customer.cpf) },
            Executable { assertEquals(OrderStatus.RECEIVED.name, order!!.status) },
            Executable { assertFalse(order!!.items.isEmpty()) },
        )
    }

    @Test
    fun `should get orders using cpf`() {
        val cpf = "47052551004"

        val customer = insertCustomerData(mockDomainCustomer(cpf = cpf))
        val product = insertProducts().first()
        val payment = insertPaymentData(mockPayment())
        orderJpaRepository.save(OrderFixtures.mockOrderEntity(customer, product, payment))

        val orders = restTemplate.exchange<List<OrderResponse>>(
            url = "/orders?cpf={cpf}",
            method = HttpMethod.GET,
            requestEntity = HttpEntity(null, authenticationHeader),
            uriVariables = mapOf(
                "cpf" to cpf,
            ),
        )

        assertAll(
            Executable { assertNotNull(orders.body) },
            Executable { assertFalse(orders.body!!.isEmpty()) },
        )

        val order = orders.body!!.first()

        assertAll(
            Executable { assertNotNull(order.id) },
            Executable { assertEquals(cpf, order.customer.cpf) },
            Executable { assertEquals(OrderStatus.NEW, order.status) },
            Executable { assertFalse(order.items.isEmpty()) },
            Executable { assertEquals(5.99.toBigDecimal(), order.total) },
        )
    }

    @Test
    fun `should return BAD_REQUEST when trying to create an order for a customer that was not found`() {
        val cpf = "44527073001"
        val items = insertProducts().map {
            OrderCreationRequest.OrderItem(
                productId = it.id!!,
                quantity = 1,
            )
        }
        val inputOrderData = OrderCreationRequest(customerCpf = cpf, items)

        val response = restTemplate.exchange<Any>(
            url = "/orders",
            method = HttpMethod.POST,
            requestEntity = HttpEntity(inputOrderData, authenticationHeader),
        )

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }
}
