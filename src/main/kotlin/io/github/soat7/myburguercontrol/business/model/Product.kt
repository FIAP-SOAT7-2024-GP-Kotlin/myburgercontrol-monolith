package io.github.soat7.myburguercontrol.business.model

import io.github.soat7.myburguercontrol.business.enum.ProductType
import java.math.BigDecimal
import java.util.UUID

data class Product(
    val id: UUID,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val type: ProductType,
)
