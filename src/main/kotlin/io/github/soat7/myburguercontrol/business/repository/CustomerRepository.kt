package io.github.soat7.myburguercontrol.business.gateway

import io.github.soat7.myburguercontrol.business.model.Customer
import java.util.UUID

interface CustomerRepository {

    fun create(customer: Customer): Customer

    fun findCustomerById(id: UUID): Customer?

    fun findCustomerByCpf(cpf: String): Customer?
}
