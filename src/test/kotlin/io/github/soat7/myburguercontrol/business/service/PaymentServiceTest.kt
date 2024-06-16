package io.github.soat7.myburguercontrol.business.service

import io.github.soat7.myburguercontrol.business.enum.PaymentStatus
import io.github.soat7.myburguercontrol.business.exception.ReasonCodeException
import io.github.soat7.myburguercontrol.business.repository.PaymentIntegrationRepository
import io.github.soat7.myburguercontrol.business.repository.PaymentRepository
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
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import io.github.soat7.myburguercontrol.business.model.Order as OrderModel

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class PaymentServiceTest {

    private val paymentIntegrationRepository = mockk<PaymentIntegrationRepository>()
    private val paymentRepository = mockk<PaymentRepository>()
    private val service = PaymentService(paymentIntegrationRepository, paymentRepository)

    @BeforeTest
    fun setUp() {
        clearMocks(paymentIntegrationRepository)
    }

    @Test
    @Order(1)
    fun `should try to pay successfully using an external service`() {
        val order = mockOrder(mockDomainCustomer(cpf = "12312312312"))

        every { paymentIntegrationRepository.requestPayment(any<OrderModel>()) } returns mockPaymentResult(
            UUID.randomUUID().toString(),
            approved = true,
        )
        every { paymentRepository.findById(any()) } returns mockPayment()
        every { paymentRepository.create(any()) } returns mockPayment()
        every { paymentRepository.update(any()) } returns mockPayment()

        val response = assertDoesNotThrow {
            service.requestPayment(order)
        }

        assertNotNull(response)
        assertEquals(PaymentStatus.APPROVED, response.status)
    }

    @Test
    @Order(1)
    fun `should try to pay denied using an external service`() {
        val order = mockOrder(mockDomainCustomer(cpf = "12312312312"))

        every {
            paymentIntegrationRepository.requestPayment(any<OrderModel>())
        } returns mockPaymentResult(null, false)

        every { paymentRepository.findById(any()) } returns mockPayment()
        every { paymentRepository.create(any()) } returns mockPayment()
        every { paymentRepository.update(any()) } returns mockPayment()

        assertThrows<ReasonCodeException> {
            service.requestPayment(order)
        }
    }
}
