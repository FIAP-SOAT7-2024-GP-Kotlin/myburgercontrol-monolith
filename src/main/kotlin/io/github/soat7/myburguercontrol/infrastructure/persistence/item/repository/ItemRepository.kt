package io.github.soat7.myburguercontrol.infrastructure.persistence.item.repository

import io.github.soat7.myburguercontrol.infrastructure.persistence.item.entity.ItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ItemRepository : JpaRepository<ItemEntity, UUID>
