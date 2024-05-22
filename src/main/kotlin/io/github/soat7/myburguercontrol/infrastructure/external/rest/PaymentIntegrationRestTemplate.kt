package io.github.soat7.myburguercontrol.infrastructure.external.rest

import mu.KLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

class PaymentIntegrationRestTemplate {

    private companion object : KLogging()

    fun requestPaymentIntegration(payment: PaymentIntegrationRequest): ResponseEntity<PaymentIntegrationResponse> {
        val restTemplate = RestTemplate()

        val uri = UriComponentsBuilder.fromUriString("\${spring.rest.payment_integration_url}")
            .path("/mercadopago/pagamento")
            .build().toUri()

        return restTemplate.postForEntity(
            uri,
            payment,
            PaymentIntegrationResponse::class.java
        ).also { logger.info { "Successfully requested integration at {$uri}" } }
    }
}
