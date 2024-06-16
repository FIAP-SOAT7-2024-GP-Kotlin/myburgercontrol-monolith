package io.github.soat7.myburguercontrol.webservice.auth.api

data class AuthRequest(
    val cpf: String,
    val password: String,
)
