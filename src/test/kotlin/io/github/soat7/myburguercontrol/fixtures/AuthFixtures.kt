package io.github.soat7.myburguercontrol.fixtures

import io.github.soat7.myburguercontrol.infrastructure.rest.api.AuthRequest

object AuthFixtures {

    fun mockAuthCreationRequest(cpf: String, password: String) = AuthRequest(
        cpf = cpf,
        password = password
    )

}
