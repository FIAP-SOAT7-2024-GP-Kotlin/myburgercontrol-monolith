package io.github.soat7.myburguercontrol.business.service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburguercontrol.business.enum.OrderStatus
import io.github.soat7.myburguercontrol.business.exception.ReasonCode
import io.github.soat7.myburguercontrol.business.exception.ReasonCodeException
import io.github.soat7.myburguercontrol.business.model.Customer
import io.github.soat7.myburguercontrol.business.model.Order
import io.github.soat7.myburguercontrol.business.model.OrderDetail
import io.github.soat7.myburguercontrol.business.model.OrderItem
import io.github.soat7.myburguercontrol.business.repository.OrderRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

private val logger = KotlinLogging.logger {}

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val customerService: CustomerService,
    private val productService: ProductService,
    private val paymentService: PaymentService,
) {

    fun createOrder(orderDetail: OrderDetail): Order {
        val customer = customerService.findCustomerByCpf(orderDetail.customerCpf)
            ?: throw ReasonCodeException(ReasonCode.CUSTOMER_NOT_FOUND)

        val items = buildOrderItems(orderDetail)

        val order = setupOrder(customer, items)

        paymentService.requestPayment(order)

        return orderRepository.update(order.copy(status = OrderStatus.RECEIVED))
    }

    fun findOrdersByCustomerCpf(cpf: String): List<Order> {
        logger.info { "Order.findOrders(cpf = $cpf)" }
        val customer = customerService.findCustomerByCpf(cpf)
            ?: throw ReasonCodeException(ReasonCode.CUSTOMER_NOT_FOUND)

        return orderRepository.findByCustomerId(customer.id)
    }

    fun findQueuedOrders(pageable: Pageable): Page<Order> {
        logger.info { "Finding orders with status: [${OrderStatus.NEW}]" }
        return orderRepository.findNewOrders(OrderStatus.NEW.name, pageable)
    }

    fun changeOrderStatus(status: OrderStatus, orderId: UUID): Order {
        return orderRepository.update(
            orderRepository.findById(orderId)?.copy(status = status)
                ?: throw ReasonCodeException(ReasonCode.ORDER_NOT_FOUND),
        )
    }

    private fun buildOrderItems(orderDetail: OrderDetail): List<OrderItem> {
        val items = orderDetail.items.map { item ->
            productService.findById(item.productId)?.let { product ->
                OrderItem(
                    id = UUID.randomUUID(),
                    product = product,
                    quantity = item.quantity,
                    comment = item.comment,
                )
            } ?: throw ReasonCodeException(ReasonCode.INVALID_PRODUCT)
        }
        return items
    }

    private fun setupOrder(
        customer: Customer,
        items: List<OrderItem>,
    ): Order {
        val order = orderRepository.create(
            Order(
                id = UUID.randomUUID(),
                customer = customer,
                items = items,
            ),
        )

        val payment = paymentService.createPayment()

        return orderRepository.update(order.copy(payment = payment))
    }
}
