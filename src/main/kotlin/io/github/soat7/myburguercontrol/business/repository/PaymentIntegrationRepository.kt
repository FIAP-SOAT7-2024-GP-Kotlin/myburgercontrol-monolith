package io.github.soat7.myburguercontrol.business.gateway

import io.github.soat7.myburguercontrol.thirdparty.api.PaymentResult
import io.github.soat7.myburguercontrol.business.model.Order

interface PaymentIntegrationRepository {

    fun requestPayment(order: Order): PaymentResult
}
