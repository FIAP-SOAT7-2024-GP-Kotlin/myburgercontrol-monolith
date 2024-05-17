package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.application.ports.inbound.OrderServicePort
import io.github.soat7.myburguercontrol.application.ports.outbound.CustomerDatabasePort
import io.github.soat7.myburguercontrol.application.ports.outbound.OrderDatabasePort
import io.github.soat7.myburguercontrol.domain.model.Customer
import io.github.soat7.myburguercontrol.domain.model.Order
import io.github.soat7.myburguercontrol.exception.CustomerNotFoundException
import mu.KLogging
import java.util.UUID

class OrderService(
    private val orderDatabasePort: OrderDatabasePort,
    private val customerDatabasePort: CustomerDatabasePort
) : OrderServicePort {
    private companion object : KLogging()

    override fun newOrder(cpf: String): Order {
        logger.info { "Order.newOrder(cpf = $cpf)" }
        val customer = customerDatabasePort.findCustomerByCpf(cpf)
            ?: throw CustomerNotFoundException(Customer(id = UUID.fromString(""), cpf = cpf))

        return newOrder(customer)
    }

    override fun newOrder(customerId: UUID): Order {
        logger.info { "Order.newOrder(customerId = $customerId)" }
        val customer = customerDatabasePort.findCustomerById(customerId)
            ?: throw CustomerNotFoundException(Customer(id = customerId, cpf = ""))

        return newOrder(customer)
    }


    private fun newOrder(customer: Customer): Order {
        val order = Order(
            id = UUID.randomUUID(),
            customer = customer,
        )

        return orderDatabasePort.create(order)
    }

    override fun findOrders(cpf: String) = run {
        logger.info { "Order.findOrders(cpf = $cpf)" }
        val customer = customerDatabasePort.findCustomerByCpf(cpf)
            ?: throw CustomerNotFoundException(Customer(id = UUID.fromString(""), cpf = cpf))

        findOrders(customer)
    }

    override fun findOrders(customerId: UUID) = run {
        logger.info { "Order.findOrders(customerId = $customerId)" }
        val customer = customerDatabasePort.findCustomerById(customerId)
            ?: throw CustomerNotFoundException(Customer(id = customerId, cpf = ""))

        findOrders(customer)
    }

    private fun findOrders(customer: Customer) = orderDatabasePort.findByCustomer(customer)
}
