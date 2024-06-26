package io.github.soat7.myburguercontrol.infrastructure.external.rest

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PaymentIntegrationRequest(
    val id: String,
    val cpf: String,
    val value: String
)
