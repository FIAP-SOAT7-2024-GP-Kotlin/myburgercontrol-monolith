package io.github.soat7.myburguercontrol.domain.mapper

import io.github.soat7.myburguercontrol.domain.enum.ProductType
import io.github.soat7.myburguercontrol.domain.model.Product
import io.github.soat7.myburguercontrol.infrastructure.persistence.product.entity.ProductEntity
import io.github.soat7.myburguercontrol.infrastructure.rest.product.api.ProductCreationRequest
import io.github.soat7.myburguercontrol.infrastructure.rest.product.api.ProductResponse
import java.time.Instant
import java.util.UUID

fun Product.toResponse() = ProductResponse(
    id = this.id,
    description = this.description,
    price = this.price,
    type = this.type.name
)

fun Product.toPersistence() = ProductEntity(
    id = this.id,
    description = this.description,
    price = this.price,
    type = this.type.name,
    createdAt = Instant.now(),
    updatedAt = Instant.now()
)

fun ProductCreationRequest.toDomain() = Product(
    id = UUID.randomUUID(),
    description = this.description,
    price = this.price,
    type = ProductType.valueOf(this.type.name)
)

fun ProductEntity.toDomain() = Product(
    id = this.id!!,
    description = this.description,
    price = this.price,
    type = ProductType.from(this.type)
)
