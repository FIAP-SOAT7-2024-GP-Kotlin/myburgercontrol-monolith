package io.github.soat7.myburguercontrol.application.ports.inbound

import io.github.soat7.myburguercontrol.domain.model.Order
import java.util.UUID

interface OrderServicePort {

    fun newOrder(cpf: String): Order
    fun newOrder(customerId: UUID): Order
    fun findOrders(cpf: String): List<Order>
    fun findOrders(customerId: UUID): List<Order>
}
