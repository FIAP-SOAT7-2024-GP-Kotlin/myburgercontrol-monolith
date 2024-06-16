package io.github.soat7.myburguercontrol.webservice.product.api

import io.github.soat7.myburguercontrol.business.enum.ProductType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class ProductCreationRequest(
    @NotBlank(message = "Product name must be specified")
    val name: String,

    @NotBlank(message = "Product description must be specified")
    val description: String,

    @NotNull(message = "A price for the product must be specified")
    val price: BigDecimal,

    @NotBlank(message = "The product type must be specified")
    val type: ProductType,
)
