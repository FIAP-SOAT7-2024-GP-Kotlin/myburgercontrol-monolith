package io.github.soat7.myburguercontrol.infrastructure.persistence.customer

import io.github.soat7.myburguercontrol.application.ports.outbound.CustomerDatabasePort
import io.github.soat7.myburguercontrol.domain.mapper.toDomain
import io.github.soat7.myburguercontrol.domain.mapper.toPersistence
import io.github.soat7.myburguercontrol.domain.model.Customer
import io.github.soat7.myburguercontrol.infrastructure.persistence.customer.repository.CustomerRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CustomerDatabaseAdapter(
    private val repository: CustomerRepository
) : CustomerDatabasePort {

    override fun create(customer: Customer): Customer = repository.save(customer.toPersistence()).toDomain()

    override fun findCustomerByCpf(cpf: String): Customer? = repository.findByCpf(cpf)?.toDomain()

    override fun findCustomerById(id: UUID): Customer? = repository.findByIdOrNull(id)?.toDomain()
}
