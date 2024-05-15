package io.github.soat7.myburguercontrol.application.ports.outbound

import io.github.soat7.myburguercontrol.domain.model.Item
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ItemDatabasePort {

    fun create(item: Item): Item

    fun findById(id: UUID): Item?

    fun findAll(pageable: Pageable): Page<Item>
}
