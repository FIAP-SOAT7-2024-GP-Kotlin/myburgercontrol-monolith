package io.github.soat7.myburguercontrol.infrastructure.external

import feign.FeignException
import io.github.soat7.myburguercontrol.application.ports.outbound.PaymentIntegrationPort
import io.github.soat7.myburguercontrol.domain.dto.PaymentResult
import io.github.soat7.myburguercontrol.domain.mapper.toDto
import io.github.soat7.myburguercontrol.domain.mapper.toRequest
import io.github.soat7.myburguercontrol.domain.model.Payment
import io.github.soat7.myburguercontrol.infrastructure.external.feign.PaymentIntegrationFeignClient
import mu.KLogging

class PaymentIntegrationService(
    private val feignClient: PaymentIntegrationFeignClient
) : PaymentIntegrationPort {

    companion object : KLogging()

    override fun requestPayment(payment: Payment): PaymentResult {
        try {
            logger.info { "Starting to integrate with payment provider with id=[${payment.id}]" }
            val response = feignClient.requestPaymentIntegration(payment.toRequest())
            return response.toDto(approved = true)
        } catch (ex: FeignException) {
            when (ex.status()) {
                402 -> return PaymentResult(null, false)
                else -> {
                    logger.error { "Integration error at payment provider operationId=[${payment.id}]" }
                    throw ex
                }
            }
        }
    }
}
