package io.github.soat7.myburguercontrol.infrastructure.rest.order.api.response

import io.github.soat7.myburguercontrol.domain.enum.OrderStatus
import io.github.soat7.myburguercontrol.infrastructure.rest.customer.api.response.CustomerResponse
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class OrderResponse(
    val id: UUID,
    val customer: CustomerResponse,
    val items: MutableList<OrderItemResponse> = mutableListOf(),
    val status: OrderStatus,
    val createdAt: Instant,
    val total: BigDecimal
)
