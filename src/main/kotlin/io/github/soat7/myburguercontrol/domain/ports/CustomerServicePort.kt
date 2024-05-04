package io.github.soat7.myburguercontrol.domain.ports

import io.github.soat7.myburguercontrol.domain.entity.CustomerEntity
import java.util.UUID

interface CustomerServicePort {

    fun create(entity: CustomerEntity): CustomerEntity

    fun findCustomerById(id: UUID): CustomerEntity?

    fun findCustomerByCpf(cpf: String): CustomerEntity?
}
