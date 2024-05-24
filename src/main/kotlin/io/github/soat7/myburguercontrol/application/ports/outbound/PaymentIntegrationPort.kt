package io.github.soat7.myburguercontrol.application.ports.outbound

import io.github.soat7.myburguercontrol.domain.dto.PaymentResult
import io.github.soat7.myburguercontrol.domain.model.Payment

interface PaymentIntegrationPort {

    fun requestPayment(payment: Payment): PaymentResult
}
