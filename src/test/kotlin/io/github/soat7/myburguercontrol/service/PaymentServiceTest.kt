package io.github.soat7.myburguercontrol.service;


import io.github.soat7.myburguercontrol.application.ports.outbound.PaymentIntegrationPort
import io.github.soat7.myburguercontrol.domain.mapper.toRequest
import io.github.soat7.myburguercontrol.domain.model.Payment
import io.github.soat7.myburguercontrol.domain.service.PaymentService
import io.github.soat7.myburguercontrol.fixtures.PaymentFixtures.mockPayment
import io.github.soat7.myburguercontrol.infrastructure.external.feign.PaymentIntegrationFeignClient
import io.github.soat7.myburguercontrol.infrastructure.external.feign.PaymentIntegrationResponse
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
public class PaymentServiceTest {

    private val paymentIntegrationPort = mockk<PaymentIntegrationPort>()
    private val service = PaymentService(paymentIntegrationPort)
    private val feignClient =  mockk<PaymentIntegrationFeignClient>()

    @BeforeTest
    fun setUp() {
        clearMocks(paymentIntegrationPort)
    }

    @Test
    @Order(1)
    fun `should try to pay successfully using an external service`(){
        val payment = mockPayment()

        val paymentIntegrationResponse = PaymentIntegrationResponse(
            message = "Payment processed successfully"
        )

        val responseEntity: ResponseEntity<PaymentIntegrationResponse> = mockk()

        every { responseEntity.statusCode } returns HttpStatus.OK
        every { responseEntity.body } returns paymentIntegrationResponse

        every {
            paymentIntegrationPort.requestPayment(any<Payment>())
        } returns true

        every {
            feignClient.requestPaymentIntegration(payment.toRequest())
        } returns responseEntity

        val response = assertDoesNotThrow {
            service.requestPayment(payment)
        }

        assertEquals(payment.id, response.id)

    }

    @Test
    @Order(1)
    fun `should try to pay denied using an external service`(){
        val payment = mockPayment()

        val paymentIntegrationResponse = PaymentIntegrationResponse(
            message = "Payment not processed"
        )

        val responseEntity: ResponseEntity<PaymentIntegrationResponse> = mockk()

        every { responseEntity.statusCode } returns HttpStatusCode.valueOf(402)
        every { responseEntity.body } returns paymentIntegrationResponse

        every {
            feignClient.requestPaymentIntegration(payment.toRequest())
        } returns responseEntity

        every {
            paymentIntegrationPort.requestPayment(any<Payment>())
        } returns false

        val response = assertDoesNotThrow {
            service.requestPayment(payment)
        }

        assertEquals(payment.status, response.status)

    }

}
