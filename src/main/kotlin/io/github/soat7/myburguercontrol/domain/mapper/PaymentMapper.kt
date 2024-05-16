package io.github.soat7.myburguercontrol.domain.mapper

import io.github.soat7.myburguercontrol.domain.model.Payment
import io.github.soat7.myburguercontrol.infrastructure.external.feign.PaymentIntegrationRequest

fun Payment.toRequest() = PaymentIntegrationRequest(operationId = this.id.toString())
