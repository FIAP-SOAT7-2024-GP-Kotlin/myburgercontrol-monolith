package io.github.soat7.myburguercontrol.fixtures

import io.github.soat7.myburguercontrol.domain.model.Item
import io.github.soat7.myburguercontrol.domain.model.Item.ItemType
import io.github.soat7.myburguercontrol.infrastructure.persistence.item.entity.ItemEntity
import io.github.soat7.myburguercontrol.infrastructure.rest.item.api.ItemCreationRequest
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

object ItemFixtures {

    fun mockItemCreationRequest() = ItemCreationRequest(
        description = "ITEM",
        price = BigDecimal("1.00"),
        type = ItemCreationRequest.ItemType.FOOD
    )

    fun mockItemEntity(id: UUID = UUID.randomUUID(), type: ItemType = ItemType.FOOD) = ItemEntity(
        id = id,
        description = "ITEM",
        price = BigDecimal("1.00"),
        type = type.name,
        createdAt = Instant.now(),
        updatedAt = Instant.now()
    )

    fun mockDomainItem(id: UUID = UUID.randomUUID(), description: String, type: ItemType = ItemType.FOOD) = Item(
        id = id,
        description = description,
        price = BigDecimal("1.00"),
        type = type
    )
}
