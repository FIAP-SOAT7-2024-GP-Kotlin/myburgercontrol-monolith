package io.github.soat7.myburguercontrol.domain.model

import io.github.soat7.myburguercontrol.domain.enum.OrderStatus
import io.github.soat7.myburguercontrol.util.DEFAULT_BIG_DECIMAL_SCALE
import io.github.soat7.myburguercontrol.util.toBigDecimal
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class Order(
    val id: UUID,
    val customer: Customer,
    val items: MutableList<OrderItem> = mutableListOf(),
    val status: OrderStatus = OrderStatus.NEW,
    val createdAt: Instant = Instant.now()
) {
    val total: BigDecimal
        get() = items.sumOf { it.product.price * it.quantity.toBigDecimal() }.setScale(DEFAULT_BIG_DECIMAL_SCALE)
}
