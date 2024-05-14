package io.github.soat7.myburguercontrol.infrastructure.config

import io.github.soat7.myburguercontrol.application.ports.outbound.CustomerDatabasePort
import io.github.soat7.myburguercontrol.application.ports.outbound.UserDatabasePort
import io.github.soat7.myburguercontrol.domain.service.CustomerService
import io.github.soat7.myburguercontrol.domain.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanConfiguration {

    @Bean
    fun customerService(customerDatabasePort: CustomerDatabasePort) = CustomerService(customerDatabasePort)

    @Bean
    fun userService(userDatabasePort: UserDatabasePort) = UserService(userDatabasePort)
}
