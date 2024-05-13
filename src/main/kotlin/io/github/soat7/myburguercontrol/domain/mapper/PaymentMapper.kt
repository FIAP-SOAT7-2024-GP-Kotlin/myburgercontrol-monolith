package io.github.soat7.myburguercontrol.domain.mapper

import io.github.soat7.myburguercontrol.domain.model.Payment
import io.github.soat7.myburguercontrol.infrastructure.outbound.rest.feign.PaymentIntegrationRequest


fun Payment.toRequest() = PaymentIntegrationRequest(operationId = this.id.toString())
