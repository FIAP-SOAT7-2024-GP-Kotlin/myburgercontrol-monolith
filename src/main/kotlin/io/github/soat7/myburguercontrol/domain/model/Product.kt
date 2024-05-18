package io.github.soat7.myburguercontrol.domain.model

import io.github.soat7.myburguercontrol.domain.enum.ProductType
import java.math.BigDecimal
import java.util.UUID

data class Product(
    val id: UUID,
    val description: String,
    val price: BigDecimal,
    val type: ProductType
)
