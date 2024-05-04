package io.github.soat7.myburguercontrol.fixtures

import io.github.soat7.myburguercontrol.domain.api.CustomerCreationRequest
import io.github.soat7.myburguercontrol.domain.api.CustomerResponse
import io.github.soat7.myburguercontrol.domain.entity.CustomerEntity
import java.util.UUID

object CustomerFixtures {

    fun mockCustomerEntity(
        id: UUID = UUID.randomUUID(),
        cpf: String,
        name: String = "TEST_NAME",
        email: String = "test@test.com"
    ) =
        CustomerEntity(
            id = id,
            cpf = cpf,
            name = name,
            email = email
        )

    fun mockCustomerCreationRequest(cpf: String) = CustomerCreationRequest(
        cpf = cpf,
        name = "TEST_NAME",
        email = "test@test.com"
    )

    fun mockCustomerResponse(id: UUID, cpf: String, name: String, email: String) = CustomerResponse(
        id = id,
        cpf = cpf,
        name = name,
        email = email
    )
}
