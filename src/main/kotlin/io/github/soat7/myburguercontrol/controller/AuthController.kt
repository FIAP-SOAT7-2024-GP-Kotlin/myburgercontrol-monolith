package io.github.soat7.myburguercontrol.controller

import io.github.soat7.myburguercontrol.dto.AuthRequest
import io.github.soat7.myburguercontrol.dto.AuthResponse
import io.github.soat7.myburguercontrol.service.AuthenticationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationService: AuthenticationService
) {
    @PostMapping
    fun autheticate(@RequestBody authRequest: AuthRequest): AuthResponse =
        authenticationService.authentication(authRequest)
}
