package io.github.soat7.myburguercontrol.application.ports.inbound

import io.github.soat7.myburguercontrol.domain.model.Item
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ItemServicePort {

    fun create(item: Item): Item

    fun findAll(pageable: Pageable): Page<Item>

    fun findById(id: UUID): Item?
}
