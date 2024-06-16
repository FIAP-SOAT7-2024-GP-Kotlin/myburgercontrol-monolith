package io.github.soat7.myburguercontrol.business.service

import io.github.soat7.myburguercontrol.config.JwtProperties
import io.github.soat7.myburguercontrol.webservice.auth.api.AuthRequest
import io.github.soat7.myburguercontrol.webservice.auth.api.AuthResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import java.util.Date

@Service
class AuthenticationService(
    private val authManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties,
) {

    fun authenticate(authRequest: AuthRequest): AuthResponse {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authRequest.cpf,
                authRequest.password,
            ),
        )
        val user = userDetailsService.loadUserByUsername(authRequest.cpf)

        val accessToken = tokenService.generate(
            userDetails = user,
            expirationDate = Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration),
        )

        return AuthResponse(accessToken)
    }
}
