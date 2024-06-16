package io.github.soat7.myburguercontrol.business.service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburguercontrol.business.enum.PaymentStatus
import io.github.soat7.myburguercontrol.business.exception.ReasonCode
import io.github.soat7.myburguercontrol.business.exception.ReasonCodeException
import io.github.soat7.myburguercontrol.business.model.Order
import io.github.soat7.myburguercontrol.business.model.Payment
import io.github.soat7.myburguercontrol.business.repository.PaymentIntegrationRepository
import io.github.soat7.myburguercontrol.business.repository.PaymentRepository
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class PaymentService(
    private val paymentIntegrationRepository: PaymentIntegrationRepository,
    private val paymentRepository: PaymentRepository,
) {

    fun createPayment(): Payment {
        logger.info { "Creating payment" }

        return paymentRepository.create(Payment())
    }

    fun requestPayment(order: Order): Payment {
        logger.info { "Starting to request payment integration for order id: [${order.id}]" }

        val payment = order.payment?.let {
            paymentRepository.findById(it.id)
        } ?: throw ReasonCodeException(ReasonCode.PAYMENT_NOT_FOUND)

        val paymentResult = paymentIntegrationRepository.requestPayment(order)

        val updatedPayment = payment.copy(
            status = checkApproval(paymentResult.approved),
            authorizationId = paymentResult.authorizationId,
        )
        paymentRepository.update(updatedPayment)

        logger.info { "Successfully integrated with status return: [${updatedPayment.status.name}]" }

        if (updatedPayment.status == PaymentStatus.DENIED) throw ReasonCodeException(ReasonCode.PAYMENT_INTEGRATION_ERROR)

        return updatedPayment
    }

    private fun checkApproval(approved: Boolean): PaymentStatus =
        if (approved) PaymentStatus.APPROVED else PaymentStatus.DENIED
}
