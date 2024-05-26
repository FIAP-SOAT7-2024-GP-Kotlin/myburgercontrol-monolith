package io.github.soat7.myburguercontrol.domain.model

import io.github.soat7.myburguercontrol.domain.enum.PaymentStatus
import java.util.UUID

data class Payment(
    val id: UUID,
    var status: PaymentStatus = PaymentStatus.REQUESTED,
    var authorizationId: String? = null
)
