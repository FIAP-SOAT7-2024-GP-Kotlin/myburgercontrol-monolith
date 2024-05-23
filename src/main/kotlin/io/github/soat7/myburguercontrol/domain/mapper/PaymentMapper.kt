package io.github.soat7.myburguercontrol.domain.mapper

import io.github.soat7.myburguercontrol.domain.dto.PaymentResult
import io.github.soat7.myburguercontrol.domain.model.Payment
import io.github.soat7.myburguercontrol.infrastructure.external.rest.PaymentIntegrationRequest
import io.github.soat7.myburguercontrol.infrastructure.external.rest.PaymentIntegrationResponse

fun Payment.toRequest() = PaymentIntegrationRequest(
    id = this.id.toString(),
    cpf = this.customer.cpf
)

fun PaymentIntegrationResponse.toDto(approved: Boolean) =
    PaymentResult(authorizationId = this.authorizationId, approved)
