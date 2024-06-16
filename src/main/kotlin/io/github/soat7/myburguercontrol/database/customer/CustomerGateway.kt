package io.github.soat7.myburguercontrol.database.customer

import io.github.soat7.myburguercontrol.business.mapper.toDomain
import io.github.soat7.myburguercontrol.business.mapper.toPersistence
import io.github.soat7.myburguercontrol.business.model.Customer
import io.github.soat7.myburguercontrol.business.repository.CustomerRepository
import io.github.soat7.myburguercontrol.database.customer.repository.CustomerJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CustomerGateway(
    private val repository: CustomerJpaRepository,
) : CustomerRepository {

    override fun create(customer: Customer): Customer = repository.save(customer.toPersistence()).toDomain()

    override fun findCustomerByCpf(cpf: String): Customer? = repository.findByCpf(cpf)?.toDomain()

    override fun findCustomerById(id: UUID): Customer? = repository.findByIdOrNull(id)?.toDomain()
}
