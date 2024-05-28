package io.github.soat7.myburguercontrol.application.ports.inbound

import io.github.soat7.myburguercontrol.domain.enum.OrderStatus
import io.github.soat7.myburguercontrol.domain.model.Order
import io.github.soat7.myburguercontrol.domain.model.OrderDetail
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface OrderServicePort {

    fun createOrder(orderDetail: OrderDetail): Order
    fun findOrdersByCustomerCpf(cpf: String): List<Order>
    fun findQueuedOrders(pageable: Pageable): Page<Order>
    fun changeOrderStatus(status: OrderStatus, orderId: UUID): Order
}
