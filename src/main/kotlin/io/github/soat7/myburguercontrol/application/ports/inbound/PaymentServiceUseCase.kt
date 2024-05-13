package io.github.soat7.myburguercontrol.application.ports.inbound

import io.github.soat7.myburguercontrol.domain.model.Payment

interface PaymentServiceUseCase {

    fun requestPayment(payment: Payment): Payment
}
