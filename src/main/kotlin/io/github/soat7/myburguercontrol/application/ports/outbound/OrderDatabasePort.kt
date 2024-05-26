package io.github.soat7.myburguercontrol.application.ports.outbound

import io.github.soat7.myburguercontrol.domain.model.Order
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface OrderDatabasePort {
    fun create(order: Order): Order
    fun findByCustomerId(customerId: UUID): List<Order>
    fun findNewOrders(status: String, pageable: Pageable): Page<Order>
}
