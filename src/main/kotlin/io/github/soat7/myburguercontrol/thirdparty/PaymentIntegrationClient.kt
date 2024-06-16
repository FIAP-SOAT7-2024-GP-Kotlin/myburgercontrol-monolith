package io.github.soat7.myburguercontrol.thirdparty

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburguercontrol.business.exception.ReasonCode
import io.github.soat7.myburguercontrol.business.exception.ReasonCodeException
import io.github.soat7.myburguercontrol.business.mapper.toDto
import io.github.soat7.myburguercontrol.business.mapper.toPaymentRequest
import io.github.soat7.myburguercontrol.business.model.Order
import io.github.soat7.myburguercontrol.business.repository.PaymentIntegrationRepository
import io.github.soat7.myburguercontrol.thirdparty.api.PaymentIntegrationResponse
import io.github.soat7.myburguercontrol.thirdparty.api.PaymentResult
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientResponseException
import org.springframework.web.client.RestTemplate

private val logger = KotlinLogging.logger {}

@Component
class PaymentIntegrationClient(
    @Value("\${third-party.payment-integration.url}") private val paymentServiceUrl: String,
    private val paymentRestTemplate: RestTemplate,
) : PaymentIntegrationRepository {

    override fun requestPayment(order: Order): PaymentResult {
        logger.info { "Starting integration with PaymentProvider" }

        try {
            val response = paymentRestTemplate.postForEntity(
                paymentServiceUrl,
                order.toPaymentRequest(),
                PaymentIntegrationResponse::class.java,
            )

            if (response.statusCode.is2xxSuccessful) {
                response.body?.let {
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
