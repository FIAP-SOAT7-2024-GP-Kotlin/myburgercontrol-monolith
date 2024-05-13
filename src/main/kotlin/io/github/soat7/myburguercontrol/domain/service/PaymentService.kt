package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.application.ports.inbound.PaymentServiceUseCase
import io.github.soat7.myburguercontrol.application.ports.outbound.PaymentIntegrationPort
import io.github.soat7.myburguercontrol.domain.model.Payment
import mu.KLogging

class PaymentService(
    private val paymentIntegrationPort: PaymentIntegrationPort
) : PaymentServiceUseCase {

    companion object : KLogging()
    override fun requestPayment(payment: Payment): Payment {

        logger.info { "Starting to request payment integration with id: [${payment.id}]" }

        if (paymentIntegrationPort.requestPayment(payment))
            payment.status = Payment.Status.APPROVED else Payment.Status.DENIED

        logger.info { "Successfully integrated with status return: [${payment.status.name}]" }

        return payment
    }
}
