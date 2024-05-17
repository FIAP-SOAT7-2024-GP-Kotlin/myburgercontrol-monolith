package io.github.soat7.myburguercontrol.application.ports.outbound

import io.github.soat7.myburguercontrol.domain.model.Customer
import io.github.soat7.myburguercontrol.domain.model.Order

interface OrderDatabasePort {
    fun create(order: Order): Order
    fun findByCustomer(customer: Customer): List<Order>
}
