package io.github.soat7.myburguercontrol.infrastructure.external

import io.github.soat7.myburguercontrol.application.ports.outbound.PaymentIntegrationPort
import io.github.soat7.myburguercontrol.domain.dto.PaymentResult
import io.github.soat7.myburguercontrol.domain.exception.ReasonCode
import io.github.soat7.myburguercontrol.domain.exception.ReasonCodeException
import io.github.soat7.myburguercontrol.domain.mapper.toDto
import io.github.soat7.myburguercontrol.domain.mapper.toPaymentRequest
import io.github.soat7.myburguercontrol.domain.model.Order
import io.github.soat7.myburguercontrol.infrastructure.external.rest.PaymentIntegrationResponse
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientResponseException
import org.springframework.web.client.RestTemplate

@Component
class PaymentIntegrationClient(
    @Value("\${third-party.payment-integration.url}") private val paymentServiceUrl: String,
    private val paymentRestTemplate: RestTemplate
) : PaymentIntegrationPort {

    private companion object : KLogging()

    override fun requestPayment(order: Order): PaymentResult {
        logger.info { "Starting integration with PaymentProvider" }

        try {
            val response = paymentRestTemplate.postForEntity(
                paymentServiceUrl,
                order.toPaymentRequest(),
                PaymentIntegrationResponse::class.java
            )

            if (response.statusCode.is2xxSuccessful) {
                response.body?.let {
                    it as PaymentIntegrationResponse
                    return it.toDto(response.statusCode.is2xxSuccessful).also {
                        logger.info { "Payment authorized" }
                    }
                } ?: run {
                    throw ReasonCodeException(ReasonCode.PAYMENT_INTEGRATION_ERROR)
                }
            } else {
                throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR)
            }
        } catch (ex: RestClientResponseException) {
            when (ex.statusCode.value()) {
                402 -> return PaymentResult(null, false).also {
                    logger.info { "Payment not authorized" }
                }

                else -> logger.warn { "Integration error" }.also { throw ex }
            }
        }
    }
}
