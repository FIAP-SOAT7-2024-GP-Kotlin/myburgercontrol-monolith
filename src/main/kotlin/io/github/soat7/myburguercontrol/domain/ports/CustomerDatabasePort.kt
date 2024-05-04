package io.github.soat7.myburguercontrol.domain.ports

import io.github.soat7.myburguercontrol.domain.entity.CustomerEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CustomerDatabasePort : JpaRepository<CustomerEntity, UUID> {

    fun findByCpf(cpf: String): CustomerEntity?
}
