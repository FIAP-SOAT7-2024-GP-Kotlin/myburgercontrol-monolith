package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.application.ports.inbound.CustomerServicePort
import io.github.soat7.myburguercontrol.application.ports.outbound.CustomerDatabasePort
import io.github.soat7.myburguercontrol.domain.model.Customer
import mu.KLogging
import java.util.UUID

class CustomerService(
    private val databasePort: CustomerDatabasePort
) : CustomerServicePort {

    private companion object : KLogging()

    override fun create(customer: Customer): Customer {
        logger.info { "Creating new customer with cpf: [${customer.cpf}]" }
        return databasePort.create(customer)
    }

    override fun findCustomerById(id: UUID): Customer? {
        logger.info { "Finding customer with id: [$id]" }
        return databasePort.findCustomerById(id)
    }

    override fun findCustomerByCpf(cpf: String): Customer? {
        logger.info { "Finding customer with cpf: [$cpf]" }
        return databasePort.findCustomerByCpf(cpf)
    }
}
