package io.github.soat7.myburguercontrol.infrastructure.external

import io.github.soat7.myburguercontrol.application.ports.outbound.PaymentIntegrationPort
import io.github.soat7.myburguercontrol.domain.dto.PaymentResult
import io.github.soat7.myburguercontrol.domain.mapper.toDto
import io.github.soat7.myburguercontrol.domain.mapper.toRequest
import io.github.soat7.myburguercontrol.domain.model.Payment
import io.github.soat7.myburguercontrol.infrastructure.external.rest.PaymentIntegrationResponse
import mu.KLogging
import org.springframework.web.client.RestClientResponseException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder


class RestTemplateService() : PaymentIntegrationPort {

    private companion object : KLogging()

    override fun requestPayment(payment: Payment): PaymentResult {

        logger.info { "Starting integration with PaymentProvider" }

        val restTemplate = RestTemplate()

        val uri = UriComponentsBuilder.fromUriString("\${spring.feign.payment_integration_url}")
            .path("/mercadopago/pagamento")
            .build().toUri()

        try {
            val response = restTemplate.postForEntity(
                uri,
                payment.toRequest(),
                PaymentIntegrationResponse::class.java
            ).also {
                logger.info { "Successfully requested integration at {$uri}" }

            }

            if (response.statusCode.is2xxSuccessful)
                response.body?.let {
                    return it.toDto(response.statusCode.is2xxSuccessful)
                } ?: run {
                    throw RuntimeException()
                }
            else throw RuntimeException()

        } catch (ex: RestClientResponseException) {
            when (ex.statusCode.value()) {
                402 -> return PaymentResult(null, false).also {
                    logger.info { "Payment not authorized" }
                }

                else -> logger.warn { "Integration error" }.also { throw ex  }
            }
        }

    }
}
