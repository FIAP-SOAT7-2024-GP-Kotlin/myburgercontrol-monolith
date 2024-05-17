package io.github.soat7.myburguercontrol.infrastructure.persistence.order.repository

import io.github.soat7.myburguercontrol.infrastructure.persistence.customer.entity.CustomerEntity
import io.github.soat7.myburguercontrol.infrastructure.persistence.order.entity.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OrderRepository : JpaRepository<OrderEntity, UUID> {

    fun findByCustomer(customer: CustomerEntity): List<OrderEntity>
}
