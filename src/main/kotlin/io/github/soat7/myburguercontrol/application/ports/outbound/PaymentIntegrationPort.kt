package io.github.soat7.myburguercontrol.application.ports.outbound

import io.github.soat7.myburguercontrol.domain.model.Payment
import io.github.soat7.myburguercontrol.infrastructure.outbound.rest.feign.PaymentIntegrationFeignClient

interface PaymentIntegrationPort {

    fun requestPayment(payment: Payment): Boolean

}
