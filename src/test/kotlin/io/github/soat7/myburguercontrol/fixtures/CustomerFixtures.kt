package io.github.soat7.myburguercontrol.fixtures

import io.github.soat7.myburguercontrol.infrastructure.outbound.customer.entity.CustomerEntity
import io.github.soat7.myburguercontrol.infrastructure.inbound.rest.api.CustomerCreationRequest
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
}
