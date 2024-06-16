package io.github.soat7.myburguercontrol.webservice.customer.api.response

import java.util.UUID

data class CustomerResponse(
    val id: UUID,
    val cpf: String,
    val name: String? = null,
    val email: String? = null,
)
