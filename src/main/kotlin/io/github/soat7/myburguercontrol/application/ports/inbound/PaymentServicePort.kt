package io.github.soat7.myburguercontrol.application.ports.inbound

import io.github.soat7.myburguercontrol.domain.model.Payment

interface PaymentServicePort {

    fun requestPayment(payment: Payment): Payment
}
