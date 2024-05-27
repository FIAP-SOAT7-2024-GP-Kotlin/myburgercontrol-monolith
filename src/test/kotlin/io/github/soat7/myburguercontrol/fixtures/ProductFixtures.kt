package io.github.soat7.myburguercontrol.fixtures

import io.github.soat7.myburguercontrol.domain.enum.ProductType
import io.github.soat7.myburguercontrol.domain.model.Product
import io.github.soat7.myburguercontrol.infrastructure.persistence.product.entity.ProductEntity
import io.github.soat7.myburguercontrol.infrastructure.rest.product.api.ProductCreationRequest
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

object ProductFixtures {

    fun mockProductCreationRequest() = ProductCreationRequest(
        name = "PRODUCT_NAME",
        description = "PRODUCT",
        price = 1.0.toBigDecimal().setScale(2),
        type = ProductType.FOOD
    )

    fun mockProductEntity(id: UUID = UUID.randomUUID(), type: ProductType = ProductType.FOOD) = ProductEntity(
        id = id,
        name = "PRODUCT_NAME",
        description = "PRODUCT",
        price = 1.0.toBigDecimal().setScale(2),
        type = type.name,
        createdAt = Instant.now(),
        updatedAt = Instant.now()
    )

    fun mockDomainProduct(
        id: UUID = UUID.randomUUID(),
        description: String = "PRODUCT_DESCRIPTION",
        type: ProductType = ProductType.FOOD,
        price: BigDecimal = 1.0.toBigDecimal().setScale(2)
    ) =
        Product(
            id = id,
            name = "PRODUCT_NAME",
            description = description,
            price = price,
            type = type
        )
}
