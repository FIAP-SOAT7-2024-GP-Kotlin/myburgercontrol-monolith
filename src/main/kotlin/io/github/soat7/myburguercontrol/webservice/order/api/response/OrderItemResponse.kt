package io.github.soat7.myburguercontrol.webservice.order.api.response

import java.math.BigDecimal
import java.util.UUID

class OrderItemResponse(
    val product: OrderItemProductResponse,
    val quantity: Int,
    val comment: String? = null,
) {
    data class OrderItemProductResponse(
        val productId: UUID,
        val name: String,
        val price: BigDecimal,
    )
}
