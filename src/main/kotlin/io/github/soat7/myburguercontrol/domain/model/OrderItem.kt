package io.github.soat7.myburguercontrol.domain.model

data class OrderItem(
    val id: Long,
    val product: Product,
    val quantity: Int
)
