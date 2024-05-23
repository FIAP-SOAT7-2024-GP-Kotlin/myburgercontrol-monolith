package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.application.ports.inbound.AuthenticationServicePort
import io.github.soat7.myburguercontrol.application.ports.inbound.CustomUserDetailsServicePort
import io.github.soat7.myburguercontrol.application.ports.inbound.TokenServicePort
import io.github.soat7.myburguercontrol.infrastructure.config.JwtProperties
import io.github.soat7.myburguercontrol.infrastructure.rest.auth.api.AuthRequest
import io.github.soat7.myburguercontrol.infrastructure.rest.auth.api.AuthResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import java.util.Date

class AuthenticationService(
    private val authManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsServicePort,
    private val tokenService: TokenServicePort,
    private val jwtProperties: JwtProperties
) : AuthenticationServicePort {

    override fun authenticate(authRequest: AuthRequest): AuthResponse {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authRequest.cpf,
                authRequest.password
            )
        )
        val user = userDetailsService.loadUserByUsername(authRequest.cpf)

        val accessToken = tokenService.generate(
            userDetails = user,
            expirationDate = Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)
        )

        return AuthResponse(accessToken)
    }
}
