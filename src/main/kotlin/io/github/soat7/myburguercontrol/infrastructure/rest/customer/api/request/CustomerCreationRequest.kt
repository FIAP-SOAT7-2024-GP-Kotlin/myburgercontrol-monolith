package io.github.soat7.myburguercontrol.infrastructure.rest.customer.api.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CustomerCreationRequest(
    @NotBlank
    @Size(max = 11)
    val cpf: String,
    @NotBlank val name: String,
    @NotBlank val email: String
)
