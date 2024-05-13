package io.github.soat7.myburguercontrol.infrastructure.config

import io.github.soat7.myburguercontrol.application.ports.outbound.CustomerDatabasePort
import io.github.soat7.myburguercontrol.application.ports.outbound.PaymentIntegrationPort
import io.github.soat7.myburguercontrol.domain.service.CustomerService
import io.github.soat7.myburguercontrol.domain.service.PaymentService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanConfiguration {

    @Bean
    fun customerService(customerDatabasePort: CustomerDatabasePort) = CustomerService(customerDatabasePort)

    @Bean
    fun paymentService(paymentIntegrationPort: PaymentIntegrationPort) = PaymentService(paymentIntegrationPort)
}
