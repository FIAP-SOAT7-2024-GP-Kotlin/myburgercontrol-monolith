package io.github.soat7.myburguercontrol.infrastructure.rest.product.api

import io.github.soat7.myburguercontrol.domain.enum.ProductType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class ProductCreationRequest(
    @NotBlank
    val description: String,

    @NotNull
    val price: BigDecimal,

    @NotBlank
    val type: ProductType
)
