package io.github.soat7.myburguercontrol.business.repository

import io.github.soat7.myburguercontrol.business.model.Payment
import java.util.UUID

interface PaymentRepository {
    fun create(payment: Payment): Payment
    fun update(payment: Payment): Payment
    fun findById(id: UUID): Payment?
}
