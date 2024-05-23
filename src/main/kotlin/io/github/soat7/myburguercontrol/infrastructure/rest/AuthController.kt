package io.github.soat7.myburguercontrol.infrastructure.rest

import io.github.soat7.myburguercontrol.application.ports.inbound.AuthenticationServicePort
import io.github.soat7.myburguercontrol.infrastructure.rest.api.AuthRequest
import io.github.soat7.myburguercontrol.infrastructure.rest.api.AuthResponse
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    path = ["/auth"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
class AuthController(
    private val authenticationService: AuthenticationServicePort
) {

    @PostMapping
    fun authenticate(@RequestBody authRequest: AuthRequest): AuthResponse =
        authenticationService.authenticate(authRequest)
}
