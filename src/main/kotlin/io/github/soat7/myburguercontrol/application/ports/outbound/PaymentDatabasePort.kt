package io.github.soat7.myburguercontrol.application.ports.outbound

import io.github.soat7.myburguercontrol.domain.model.Payment
import java.util.UUID

interface PaymentDatabasePort {
    fun create(payment: Payment): Payment
    fun update(payment: Payment): Payment
    fun findById(id: UUID): Payment?
}
