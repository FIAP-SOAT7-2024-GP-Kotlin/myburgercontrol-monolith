package io.github.soat7.myburguercontrol.infrastructure.rest.user.api

data class AuthRequest(
    val cpf: String,
    val password: String
)
