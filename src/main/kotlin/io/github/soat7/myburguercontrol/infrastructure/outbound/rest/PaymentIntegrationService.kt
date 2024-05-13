package io.github.soat7.myburguercontrol.infrastructure.outbound.rest

import feign.FeignException
import io.github.soat7.myburguercontrol.application.ports.outbound.PaymentIntegrationPort
import io.github.soat7.myburguercontrol.domain.mapper.toRequest
import io.github.soat7.myburguercontrol.domain.model.Payment
import io.github.soat7.myburguercontrol.infrastructure.outbound.rest.feign.PaymentIntegrationFeignClient
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.HttpStatusCode

class PaymentIntegrationService(
    private val feignClient: PaymentIntegrationFeignClient
) : PaymentIntegrationPort {

    override fun requestPayment(payment: Payment): Boolean {

        try {
            val integrationResponse = feignClient.requestPaymentIntegration(payment.toRequest())
            return integrationResponse.statusCode == HttpStatusCode.valueOf(200)

        } catch (ex: FeignException) {
            if (ex.status() == 404) throw NotFoundException()
            if (ex.status() in 400..499) throw ex
            return false
        }
    }

}
