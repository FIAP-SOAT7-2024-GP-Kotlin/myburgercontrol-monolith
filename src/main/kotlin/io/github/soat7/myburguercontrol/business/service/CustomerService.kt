package io.github.soat7.myburguercontrol.business.service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburguercontrol.business.exception.ReasonCode
import io.github.soat7.myburguercontrol.business.exception.ReasonCodeException
import io.github.soat7.myburguercontrol.business.model.Customer
import io.github.soat7.myburguercontrol.business.repository.CustomerRepository
import org.springframework.stereotype.Service
import java.util.UUID

private val logger = KotlinLogging.logger {}

@Service
class CustomerService(
    private val databasePort: CustomerRepository,
) {

    fun create(customer: Customer): Customer = try {
        logger.info { "Creating new customer with cpf: [${customer.cpf}]" }
        validateExistingCpf(customer.cpf)

        databasePort.create(customer)
    } catch (ex: ReasonCodeException) {
        throw ex
    } catch (ex: Exception) {
        logger.error(ex) { "Error while creating customer" }
        throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR, ex)
    }

    fun findCustomerById(id: UUID): Customer? = try {
        logger.info { "Finding customer with id: [$id]" }
        databasePort.findCustomerById(id)
    } catch (ex: Exception) {
        logger.error(ex) { "Error while finding customer by id" }
        throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR, ex)
    }

    fun findCustomerByCpf(cpf: String): Customer? = try {
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
