package io.github.soat7.myburguercontrol.application.ports.outbound

import io.github.soat7.myburguercontrol.domain.model.Payment

interface PaymentDatabasePort {
    fun create(payment: Payment): Payment
}
