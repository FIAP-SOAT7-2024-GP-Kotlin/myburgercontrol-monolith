package io.github.soat7.myburguercontrol.infrastructure.config

import io.github.soat7.myburguercontrol.application.ports.inbound.AuthenticationServicePort
import io.github.soat7.myburguercontrol.application.ports.inbound.CustomUserDetailsServicePort
import io.github.soat7.myburguercontrol.application.ports.inbound.TokenServicePort
import io.github.soat7.myburguercontrol.application.ports.outbound.CustomerDatabasePort
import io.github.soat7.myburguercontrol.application.ports.outbound.UserDatabasePort
import io.github.soat7.myburguercontrol.domain.service.AuthenticationService
import io.github.soat7.myburguercontrol.domain.service.CustomerService
import io.github.soat7.myburguercontrol.domain.service.TokenService
import io.github.soat7.myburguercontrol.domain.service.UserService
import io.github.soat7.myburguercontrol.infrastructure.persistence.user.repository.UserRepository
import io.github.soat7.myburguercontrol.domain.service.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.authentication.AuthenticationManager

@Configuration
class BeanConfiguration(
    private val authManager: AuthenticationManager,
    private val jwtProperties: JwtProperties,
    private val userRepository: UserRepository
) {

    @Bean
    fun customerService(customerDatabasePort: CustomerDatabasePort) = CustomerService(customerDatabasePort)

    @Bean
    fun userService(userDatabasePort: UserDatabasePort,
                    encoder: PasswordEncoder
    ) = UserService(userDatabasePort, encoder)

    @Bean
    fun authenticationService(
        userDetailsService: CustomUserDetailsServicePort,
        tokenService: TokenServicePort
    ): AuthenticationServicePort {
        return AuthenticationService(authManager, userDetailsService, tokenService, jwtProperties)
    }

    @Bean
    fun customUserDetailsService(): CustomUserDetailsServicePort {
        return CustomUserDetailsService(userRepository)
    }

    @Bean
    fun tokenService(): TokenServicePort {
        return TokenService(jwtProperties)
    }
}
