package io.github.soat7.myburguercontrol.infrastructure.persistence.item

import io.github.soat7.myburguercontrol.infrastructure.persistence.item.repository.ItemRepository
import org.springframework.stereotype.Component

@Component
class ItemDatabaseAdapter(
    private val itemRepository: ItemRepository
) {
}
