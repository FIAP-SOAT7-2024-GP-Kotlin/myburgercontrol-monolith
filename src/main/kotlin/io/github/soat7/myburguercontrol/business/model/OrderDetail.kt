package io.github.soat7.myburguercontrol.business.model

import java.util.UUID

data class OrderDetail(
    val customerCpf: String,
    val items: List<OrderItemDetail>,
    val comment: String? = null,
) {
    data class OrderItemDetail(
        val productId: UUID,
        val quantity: Int,
        val comment: String? = null,
    )
}
