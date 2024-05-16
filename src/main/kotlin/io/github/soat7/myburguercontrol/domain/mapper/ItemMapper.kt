package io.github.soat7.myburguercontrol.domain.mapper

import io.github.soat7.myburguercontrol.domain.enum.ItemType
import io.github.soat7.myburguercontrol.domain.model.Item
import io.github.soat7.myburguercontrol.infrastructure.persistence.item.entity.ItemEntity
import io.github.soat7.myburguercontrol.infrastructure.rest.item.api.ItemCreationRequest
import io.github.soat7.myburguercontrol.infrastructure.rest.item.api.ItemResponse
import java.time.Instant
import java.util.UUID

fun Item.toResponse() = ItemResponse(
    id = this.id,
    description = this.description,
    price = this.price,
    type = this.type.name
)

fun Item.toPersistence() = ItemEntity(
    id = this.id,
    description = this.description,
    price = this.price,
    type = this.type.name,
    createdAt = Instant.now(),
    updatedAt = Instant.now()
)

fun ItemCreationRequest.toDomain() = Item(
    id = UUID.randomUUID(),
    description = this.description,
    price = this.price,
    type = ItemType.valueOf(this.type.name)
)

fun ItemEntity.toDomain() = Item(
    id = this.id!!,
    description = this.description,
    price = this.price,
    type = ItemType.from(this.type)
)
