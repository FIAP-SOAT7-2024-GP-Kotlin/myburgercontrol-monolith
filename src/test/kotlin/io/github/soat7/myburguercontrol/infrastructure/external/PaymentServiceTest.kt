package io.github.soat7.myburguercontrol.infrastructure.external

import io.github.soat7.myburguercontrol.application.ports.outbound.PaymentIntegrationPort
import io.github.soat7.myburguercontrol.domain.model.Payment
import io.github.soat7.myburguercontrol.domain.service.PaymentService
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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class PaymentServiceTest {

    private val paymentIntegrationPort = mockk<PaymentIntegrationPort>()
    private val service = PaymentService(paymentIntegrationPort)

    @BeforeTest
    fun setUp() {
        clearMocks(paymentIntegrationPort)
    }

    @Test
    @Order(1)
    fun `should try to pay successfully using an external service`() {
        val payment = mockPayment()

        every {
            paymentIntegrationPort.requestPayment(any<Payment>())
        } returns mockPaymentResult(UUID.randomUUID().toString(), true)

        val response = assertDoesNotThrow {
            service.requestPayment(payment)
        }

        assertEquals(payment.id, response.id)

    }

    @Test
    @Order(1)
    fun `should try to pay denied using an external service`() {
        val payment = mockPayment()

        every {
            paymentIntegrationPort.requestPayment(any<Payment>())
        } returns mockPaymentResult(null, false)

        val response = assertDoesNotThrow {
            service.requestPayment(payment)
        }

        assertEquals(payment.status, response.status)
    }
}
