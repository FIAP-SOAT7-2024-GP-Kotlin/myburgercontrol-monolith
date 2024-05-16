package io.github.soat7.myburguercontrol.infrastructure.persistence.customer.repository

import io.github.soat7.myburguercontrol.infrastructure.persistence.customer.entity.CustomerEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CustomerRepository : JpaRepository<CustomerEntity, UUID> {

    fun findByCpf(cpf: String): CustomerEntity?
}
