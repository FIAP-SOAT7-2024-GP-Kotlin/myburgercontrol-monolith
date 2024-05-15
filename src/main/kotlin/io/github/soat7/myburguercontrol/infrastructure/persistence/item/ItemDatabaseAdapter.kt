package io.github.soat7.myburguercontrol.infrastructure.persistence.item

import io.github.soat7.myburguercontrol.application.ports.outbound.ItemDatabasePort
import io.github.soat7.myburguercontrol.domain.mapper.toDomain
import io.github.soat7.myburguercontrol.domain.mapper.toPersistence
import io.github.soat7.myburguercontrol.domain.model.Item
import io.github.soat7.myburguercontrol.infrastructure.persistence.item.repository.ItemRepository
import mu.KLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ItemDatabaseAdapter(
    private val repository: ItemRepository
) : ItemDatabasePort {

    private companion object : KLogging()

    override fun create(item: Item): Item = try {
        repository
            .save(item.toPersistence()).toDomain()
    } catch (ex: Exception) {
        logger.error(ex) { "An error occurred while creating new item: ${ex.message}" }
        throw ex
    }

    override fun findById(id: UUID): Item? = repository.findByIdOrNull(id)?.toDomain()

    override fun findAll(pageable: Pageable): Page<Item> = repository.findAll(pageable)
        .map { it.toDomain() }
}
