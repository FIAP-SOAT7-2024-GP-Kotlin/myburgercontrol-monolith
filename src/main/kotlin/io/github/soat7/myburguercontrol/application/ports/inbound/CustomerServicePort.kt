package io.github.soat7.myburguercontrol.application.ports.inbound

import io.github.soat7.myburguercontrol.domain.model.Customer
import java.util.UUID

interface CustomerServicePort {

    fun create(customer: Customer): Customer

    fun findCustomerById(id: UUID): Customer?

    fun findCustomerByCpf(cpf: String): Customer?
}
