package io.github.soat7.myburguercontrol.application.ports.inbound

import io.github.soat7.myburguercontrol.domain.model.Order
import io.github.soat7.myburguercontrol.domain.model.OrderDetail
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface OrderServicePort {

    fun createOrder(orderDetail: OrderDetail): Order
    fun findOrdersByCustomerCpf(cpf: String): List<Order>
    fun findQueueOrders(pageable: Pageable): Page<Order>
}
