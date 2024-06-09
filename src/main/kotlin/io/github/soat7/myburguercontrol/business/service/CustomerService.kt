package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.application.ports.inbound.CustomerServicePort
import io.github.soat7.myburguercontrol.application.ports.outbound.CustomerDatabasePort
import io.github.soat7.myburguercontrol.domain.exception.ReasonCode
import io.github.soat7.myburguercontrol.domain.exception.ReasonCodeException
import io.github.soat7.myburguercontrol.domain.model.Customer
import mu.KLogging
import java.util.UUID

class CustomerService(
    private val databasePort: CustomerDatabasePort
) : CustomerServicePort {

    private companion object : KLogging()

    override fun create(customer: Customer): Customer = try {
        logger.info { "Creating new customer with cpf: [${customer.cpf}]" }
        validateExistingCpf(customer.cpf)

        databasePort.create(customer)
    } catch (ex: ReasonCodeException) {
        throw ex
    } catch (ex: Exception) {
        logger.error(ex) { "Error while creating customer" }
        throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR, ex)
    }

    override fun findCustomerById(id: UUID): Customer? = try {
        logger.info { "Finding customer with id: [$id]" }
        databasePort.findCustomerById(id)
    } catch (ex: Exception) {
        logger.error(ex) { "Error while finding customer by id" }
        throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR, ex)
    }

    override fun findCustomerByCpf(cpf: String): Customer? = try {
        logger.info { "Finding customer with cpf: [$cpf]" }
        databasePort.findCustomerByCpf(cpf)
    } catch (ex: Exception) {
        logger.error(ex) { "Error while finding customer by cpf" }
        throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR, ex)
    }

    private fun validateExistingCpf(cpf: String) {
        if (findCustomerByCpf(cpf) != null) {
            throw ReasonCodeException(ReasonCode.CPF_ALREADY_REGISTERED)
        }
    }
}
