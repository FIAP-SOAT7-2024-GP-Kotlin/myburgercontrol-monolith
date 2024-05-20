package io.github.soat7.myburguercontrol.domain.model

import io.github.soat7.myburguercontrol.domain.enum.PaymentStatus
import java.math.BigDecimal
import java.util.UUID

data class Payment(
    val id: UUID,
    var status: PaymentStatus = PaymentStatus.REQUESTED,
    val value: BigDecimal,
    var authorizationId: String?,
    val customer: Customer
)
