package io.github.soat7.myburguercontrol.application.ports.outbound

import io.github.soat7.myburguercontrol.domain.dto.PaymentResult
import io.github.soat7.myburguercontrol.domain.model.Order

interface PaymentIntegrationPort {

    fun requestPayment(order: Order): PaymentResult
}
