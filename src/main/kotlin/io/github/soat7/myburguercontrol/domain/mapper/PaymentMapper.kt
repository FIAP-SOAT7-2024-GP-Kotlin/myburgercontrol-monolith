package io.github.soat7.myburguercontrol.domain.mapper

import io.github.soat7.myburguercontrol.domain.dto.PaymentResult
import io.github.soat7.myburguercontrol.domain.enum.PaymentStatus
import io.github.soat7.myburguercontrol.domain.model.Payment
import io.github.soat7.myburguercontrol.infrastructure.external.rest.PaymentIntegrationResponse
import io.github.soat7.myburguercontrol.infrastructure.persistence.payment.entity.PaymentEntity

fun PaymentIntegrationResponse.toDto(approved: Boolean) =
    PaymentResult(authorizationId = this.authorizationId, approved)

fun Payment.toPersistence() = PaymentEntity(
    id = this.id,
    authorizationId = this.authorizationId,
    status = this.status.toString(),
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)

fun PaymentEntity.toDomain() = Payment(
    id = this.id,
    authorizationId = this.authorizationId,
    status = PaymentStatus.valueOf(this.status),
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)
