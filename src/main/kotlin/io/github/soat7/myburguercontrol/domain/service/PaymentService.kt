package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.application.ports.inbound.PaymentServicePort
import io.github.soat7.myburguercontrol.application.ports.outbound.PaymentIntegrationPort
import io.github.soat7.myburguercontrol.domain.enum.PaymentStatus
import io.github.soat7.myburguercontrol.domain.model.Payment
import mu.KLogging

class PaymentService(
    private val paymentIntegrationPort: PaymentIntegrationPort
) : PaymentServicePort {

    companion object : KLogging()

    override fun requestPayment(payment: Payment): Payment {
        logger.info { "Starting to request payment integration with id: [${payment.id}]" }

        val paymentResult = paymentIntegrationPort.requestPayment(payment)

        payment.apply {
            this.authorizationId = paymentResult.authorizationId
            this.status = checkApproval(paymentResult.approved)

        }

        logger.info { "Successfully integrated with status return: [${payment.status.name}]" }

        return payment
    }

    private fun checkApproval(approved: Boolean): PaymentStatus =
        if (approved) PaymentStatus.APPROVED else PaymentStatus.DENIED
}
