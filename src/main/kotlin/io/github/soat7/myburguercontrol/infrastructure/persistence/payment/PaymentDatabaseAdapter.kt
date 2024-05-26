package io.github.soat7.myburguercontrol.infrastructure.persistence.payment

import io.github.soat7.myburguercontrol.application.ports.outbound.PaymentDatabasePort
import io.github.soat7.myburguercontrol.domain.mapper.toDomain
import io.github.soat7.myburguercontrol.domain.mapper.toPersistence
import io.github.soat7.myburguercontrol.domain.model.Payment
import io.github.soat7.myburguercontrol.infrastructure.persistence.payment.repository.PaymentRepository

class PaymentDatabaseAdapter(
    private val repository: PaymentRepository,
) : PaymentDatabasePort {
    override fun create(payment: Payment): Payment {
        return repository.save(payment.toPersistence()).toDomain()
    }
}
