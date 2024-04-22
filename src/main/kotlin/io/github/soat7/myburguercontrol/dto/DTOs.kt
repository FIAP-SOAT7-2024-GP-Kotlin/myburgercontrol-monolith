package io.github.soat7.myburguercontrol.dto

import jakarta.validation.constraints.NotBlank

data class ClientDTO(
    val id: Long? = 0,
    @NotBlank val cpf: String
)