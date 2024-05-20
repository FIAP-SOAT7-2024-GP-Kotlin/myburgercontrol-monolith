package io.github.soat7.myburguercontrol.infrastructure.external

import io.github.soat7.myburguercontrol.application.ports.outbound.PaymentIntegrationPort
import io.github.soat7.myburguercontrol.domain.dto.PaymentResult
import io.github.soat7.myburguercontrol.domain.mapper.toDto
import io.github.soat7.myburguercontrol.domain.mapper.toRequest
import io.github.soat7.myburguercontrol.domain.model.Payment
import io.github.soat7.myburguercontrol.infrastructure.external.rest.PaymentIntegrationResponse
import io.github.soat7.myburguercontrol.infrastructure.external.rest.PaymentIntegrationRestTemplate
import mu.KLogging
import org.springframework.web.client.RestClientResponseException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder


class PaymentIntegrationService(
    val paymentIntegrationRestTemplate: PaymentIntegrationRestTemplate
) : PaymentIntegrationPort {

    private companion object : KLogging()

    override fun requestPayment(payment: Payment): PaymentResult {

        logger.info { "Starting integration with PaymentProvider" }

        try {
            val response = paymentIntegrationRestTemplate.requestPaymentIntegration(payment.toRequest())

            if (response.statusCode.is2xxSuccessful)
                response.body?.let {
                    return it.toDto(response.statusCode.is2xxSuccessful).also {
                        logger.info { "Payment authorized" }
                    }
                } ?: run {
                    throw RuntimeException()
                }
            else throw RuntimeException()

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
