package io.github.soat7.myburguercontrol.config

import io.github.soat7.myburguercontrol.domain.ports.CustomerDatabasePort
import io.github.soat7.myburguercontrol.domain.service.CustomerService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanConfiguration {

    @Bean
    fun customerService(customerDatabasePort: CustomerDatabasePort): CustomerService {
        return CustomerService(customerDatabasePort)
    }
}
