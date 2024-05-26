package io.github.soat7.myburguercontrol.domain.model

import io.github.soat7.myburguercontrol.domain.enum.PaymentStatus
import java.time.Instant
import java.util.UUID

data class Payment(
    val id: UUID = UUID.randomUUID(),
    val status: PaymentStatus = PaymentStatus.REQUESTED,
    val authorizationId: String? = null,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)
