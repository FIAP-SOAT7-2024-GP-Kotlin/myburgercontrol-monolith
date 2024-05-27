package io.github.soat7.myburguercontrol.fixtures

import io.github.soat7.myburguercontrol.infrastructure.external.rest.PaymentIntegrationResponse
import java.util.UUID

object PaymentResponse {

    fun approvedResponseBody() = PaymentIntegrationResponse(
        message = "Pagamento autorizado",
        authorizationId = UUID.randomUUID().toString()
    )
}
