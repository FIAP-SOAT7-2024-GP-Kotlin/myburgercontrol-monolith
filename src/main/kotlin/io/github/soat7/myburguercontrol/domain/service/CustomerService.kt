package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.domain.entity.CustomerEntity
import io.github.soat7.myburguercontrol.domain.ports.CustomerDatabasePort
import io.github.soat7.myburguercontrol.domain.ports.CustomerServicePort
import mu.KLogging
import org.springframework.data.repository.findByIdOrNull
import java.util.UUID

class CustomerService(
    private val repository: CustomerDatabasePort
) : CustomerServicePort {

    companion object : KLogging()

    override fun create(entity: CustomerEntity): CustomerEntity {
        logger.info { "Creating new customer with cpf: [${entity.cpf}]" }
        return repository.save(entity)
    }

    override fun findCustomerById(id: UUID): CustomerEntity? {
        logger.info { "Finding customer with id: [$id]" }
        return repository.findByIdOrNull(id)
    }

    override fun findCustomerByCpf(cpf: String): CustomerEntity? {
        logger.info { "Finding customer with cpf: [$cpf]" }
        return repository.findByCpf(cpf)
    }
}
