package io.github.soat7.myburguercontrol.application.ports.outbound

import io.github.soat7.myburguercontrol.domain.model.Customer
import java.util.UUID

interface CustomerDatabasePort {

    fun create(customer: Customer): Customer

    fun findCustomerById(id: UUID): Customer?

    fun findCustomerByCpf(cpf: String): Customer?
}
