package io.github.soat7.myburguercontrol.service

import io.github.soat7.myburguercontrol.config.JwtProperties
import io.github.soat7.myburguercontrol.dto.AuthRequest
import io.github.soat7.myburguercontrol.dto.AuthResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import java.util.Date

@Service
class AuthenticationService(
    private val authManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties
) {
    fun authentication(authRequest: AuthRequest): AuthResponse {
        //  Lança 403 Forbiden caso não de math
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authRequest.email,
                authRequest.password
            )
        )

        //  Carrega usuário
        val user = userDetailsService.loadUserByUsername(authRequest.email)

        //  Carrega token
        val accessToken = tokenService.generate(
            userDetails = user,
            expirationDate = Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)
        )

        return AuthResponse(accessToken)
    }
}
