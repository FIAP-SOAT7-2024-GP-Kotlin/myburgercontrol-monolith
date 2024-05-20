package io.github.soat7.myburguercontrol.application.ports.inbound

import io.github.soat7.myburguercontrol.infrastructure.rest.api.AuthRequest
import io.github.soat7.myburguercontrol.infrastructure.rest.api.AuthResponse

interface AuthenticationServicePort {
    fun authenticate(authRequest: AuthRequest): AuthResponse
}
