package io.github.soat7.myburguercontrol.domain.dto

data class PaymentResult(
    val authorizationId: String?,
    val approved: Boolean
)
