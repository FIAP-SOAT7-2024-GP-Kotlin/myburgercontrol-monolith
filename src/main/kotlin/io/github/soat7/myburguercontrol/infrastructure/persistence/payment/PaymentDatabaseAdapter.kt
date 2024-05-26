package io.github.soat7.myburguercontrol.infrastructure.persistence.payment

import io.github.soat7.myburguercontrol.application.ports.outbound.PaymentDatabasePort
import io.github.soat7.myburguercontrol.domain.mapper.toDomain
import io.github.soat7.myburguercontrol.domain.mapper.toPersistence
import io.github.soat7.myburguercontrol.domain.model.Payment
import io.github.soat7.myburguercontrol.infrastructure.persistence.payment.repository.PaymentRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Component
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Component
class PaymentDatabaseAdapter(
    private val repository: PaymentRepository
) : PaymentDatabasePort {

    override fun create(payment: Payment): Payment {
        return repository.save(payment.toPersistence()).toDomain()
    }

    override fun findById(id: UUID): Payment? {
        return repository.findById(id).getOrNull()?.toDomain()
    }

    override fun update(payment: Payment): Payment {
        findById(payment.id) ?: throw EntityNotFoundException("Payment with id ${payment.id} not found")

        return repository.save(payment.toPersistence()).toDomain()
    }
}
