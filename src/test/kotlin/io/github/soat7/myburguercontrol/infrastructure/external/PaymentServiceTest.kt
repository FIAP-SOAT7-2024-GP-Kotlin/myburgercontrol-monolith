package io.github.soat7.myburguercontrol.infrastructure.external

import io.github.soat7.myburguercontrol.application.ports.outbound.PaymentDatabasePort
import io.github.soat7.myburguercontrol.application.ports.outbound.PaymentIntegrationPort
import io.github.soat7.myburguercontrol.domain.service.PaymentService
import io.github.soat7.myburguercontrol.fixtures.CustomerFixtures.mockDomainCustomer
import io.github.soat7.myburguercontrol.fixtures.OrderFixtures.mockOrder
import io.github.soat7.myburguercontrol.fixtures.PaymentFixtures.mockPayment
import io.github.soat7.myburguercontrol.fixtures.PaymentResultFixtures.mockPaymentResult
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.assertDoesNotThrow
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import io.github.soat7.myburguercontrol.domain.model.Order as OrderModel

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class PaymentServiceTest {

    private val paymentIntegrationPort = mockk<PaymentIntegrationPort>()
    private val paymentDatabasePort = mockk<PaymentDatabasePort>()
    private val service = PaymentService(paymentIntegrationPort, paymentDatabasePort)

    @BeforeTest
    fun setUp() {
        clearMocks(paymentIntegrationPort)
    }

    @Test
    @Order(1)
    fun `should try to pay successfully using an external service`() {
        val order = mockOrder(mockDomainCustomer(cpf = "12312312312"))

        every { paymentIntegrationPort.requestPayment(any<OrderModel>()) } returns mockPaymentResult(
            UUID.randomUUID().toString(), approved = true
        )

        every { paymentDatabasePort.create(any()) } returns mockPayment()

        every { paymentDatabasePort.findById(any()) } returns mockPayment()

        val response = assertDoesNotThrow {
            service.requestPayment(order)
        }

        assertEquals(order.id, response.id)
    }

    @Test
    @Order(1)
    fun `should try to pay denied using an external service`() {
        val order = mockOrder(mockDomainCustomer(cpf = "12312312312"))

        every {
            paymentIntegrationPort.requestPayment(any<OrderModel>())
        } returns mockPaymentResult(null, false)

        val response = assertDoesNotThrow {
            service.requestPayment(order)
        }

        assertEquals(order.payment?.status, response.status)
    }
}
