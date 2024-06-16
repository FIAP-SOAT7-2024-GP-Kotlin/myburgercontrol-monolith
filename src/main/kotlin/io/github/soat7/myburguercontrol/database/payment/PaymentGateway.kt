package io.github.soat7.myburguercontrol.database.payment

import io.github.soat7.myburguercontrol.business.mapper.toDomain
import io.github.soat7.myburguercontrol.business.mapper.toPersistence
import io.github.soat7.myburguercontrol.business.model.Payment
import io.github.soat7.myburguercontrol.business.repository.PaymentRepository
import io.github.soat7.myburguercontrol.database.payment.repository.PaymentJpaRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Component
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Component
class PaymentGateway(
    private val repository: PaymentJpaRepository,
) : PaymentRepository {

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
