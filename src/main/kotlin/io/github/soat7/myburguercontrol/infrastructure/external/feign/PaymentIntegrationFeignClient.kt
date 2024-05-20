package io.github.soat7.myburguercontrol.infrastructure.external.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "PaymentIntegrationFeignClient", url = "\${spring.feign.payment_integration_url}")
interface PaymentIntegrationFeignClient {
    @PostMapping("/pagamento/mercadopago")
    fun requestPaymentIntegration(
        @RequestBody paymentIntegrationRequest: PaymentIntegrationRequest
    ): PaymentIntegrationResponse
}
