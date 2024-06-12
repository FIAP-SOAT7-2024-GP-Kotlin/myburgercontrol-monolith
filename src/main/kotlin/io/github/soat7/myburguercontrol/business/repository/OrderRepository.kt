package io.github.soat7.myburguercontrol.business.repository

import io.github.soat7.myburguercontrol.business.model.Order
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface OrderRepository {
    fun create(order: Order): Order
    fun findByCustomerId(customerId: UUID): List<Order>
    fun findNewOrders(status: String, pageable: Pageable): Page<Order>
    fun update(order: Order): Order
    fun findById(orderId: UUID): Order?
}
