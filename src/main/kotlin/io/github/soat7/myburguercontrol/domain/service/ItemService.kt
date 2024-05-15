package io.github.soat7.myburguercontrol.domain.service

import io.github.soat7.myburguercontrol.application.ports.inbound.ItemServicePort
import io.github.soat7.myburguercontrol.application.ports.outbound.ItemDatabasePort
import io.github.soat7.myburguercontrol.domain.model.Item
import mu.KLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

class ItemService(
    private val databasePort: ItemDatabasePort
) : ItemServicePort {

    private companion object : KLogging()

    override fun create(item: Item): Item {
        logger.debug { "Saving new item $item" }
        return databasePort.create(item)
    }

    override fun findAll(pageable: Pageable): Page<Item> {
        logger.debug { "Finding all items" }
        return databasePort.findAll(pageable)
    }

    override fun findById(id: UUID): Item? {
        logger.debug { "Finding item with id $id" }
        return databasePort.findById(id)
    }
}
