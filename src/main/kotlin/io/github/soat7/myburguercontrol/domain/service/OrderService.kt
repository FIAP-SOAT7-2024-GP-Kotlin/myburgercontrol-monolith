package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.application.ports.inbound.CustomerServicePort
import io.github.soat7.myburguercontrol.application.ports.inbound.OrderServicePort
import io.github.soat7.myburguercontrol.application.ports.inbound.ProductServicePort
import io.github.soat7.myburguercontrol.application.ports.outbound.OrderDatabasePort
import io.github.soat7.myburguercontrol.domain.enum.OrderStatus
import io.github.soat7.myburguercontrol.domain.exception.ReasonCode
import io.github.soat7.myburguercontrol.domain.exception.ReasonCodeException
import io.github.soat7.myburguercontrol.domain.model.Order
import io.github.soat7.myburguercontrol.domain.model.OrderDetail
import io.github.soat7.myburguercontrol.domain.model.OrderItem
import mu.KLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

class OrderService(
    private val orderDatabasePort: OrderDatabasePort,
    private val customerService: CustomerServicePort,
    private val productService: ProductServicePort
) : OrderServicePort {

    private companion object : KLogging()

    override fun createOrder(orderDetail: OrderDetail): Order {
        val customer = customerService.findCustomerByCpf(orderDetail.customerCpf)
            ?: throw ReasonCodeException(ReasonCode.CUSTOMER_NOT_FOUND)

        val items = orderDetail.items.map { item ->
            productService.findById(item.productId)?.let { product ->
                OrderItem(
                    id = UUID.randomUUID(),
                    product = product,
                    quantity = item.quantity,
                    comment = item.comment
                )
            } ?: throw ReasonCodeException(ReasonCode.INVALID_PRODUCT)
        }

        return orderDatabasePort.create(
            Order(
                id = UUID.randomUUID(),
                customer = customer,
                items = items
            )
        )
    }

    override fun findOrdersByCustomerCpf(cpf: String) = run {
        logger.info { "Order.findOrders(cpf = $cpf)" }
        val customer = customerService.findCustomerByCpf(cpf)
            ?: throw ReasonCodeException(ReasonCode.CUSTOMER_NOT_FOUND)

        orderDatabasePort.findByCustomerId(customer.id)
    }

    override fun findQueuedOrders(pageable: Pageable): Page<Order> {
        logger.info { "Finding orders with status: [${OrderStatus.NEW}]" }
        return orderDatabasePort.findNewOrders(OrderStatus.NEW.name, pageable)
    }
}
