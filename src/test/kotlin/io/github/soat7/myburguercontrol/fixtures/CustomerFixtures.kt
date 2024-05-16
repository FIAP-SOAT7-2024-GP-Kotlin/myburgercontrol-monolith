package io.github.soat7.myburguercontrol.fixtures

import io.github.soat7.myburguercontrol.domain.model.Customer
import io.github.soat7.myburguercontrol.infrastructure.persistence.customer.entity.CustomerEntity
import io.github.soat7.myburguercontrol.infrastructure.rest.api.CustomerCreationRequest
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

    fun mockCustomer(id: UUID = UUID.randomUUID(), cpf: String): Customer {
        val entity = mockCustomerEntity(id = id, cpf = cpf)

        return Customer(
            id = entity.id!!,
            cpf = entity.cpf,
            name = entity.name,
            email = entity.email
        )
    }
}
