package io.github.soat7.myburguercontrol.thirdparty.api

data class PaymentResult(
    val authorizationId: String?,
    val approved: Boolean,
)
