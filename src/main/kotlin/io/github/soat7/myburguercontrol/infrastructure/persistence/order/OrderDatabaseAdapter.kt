package io.github.soat7.myburguercontrol.infrastructure.persistence.order

import io.github.soat7.myburguercontrol.application.ports.outbound.OrderDatabasePort
import io.github.soat7.myburguercontrol.domain.mapper.toDomain
import io.github.soat7.myburguercontrol.domain.mapper.toPersistence
import io.github.soat7.myburguercontrol.domain.model.Customer
import io.github.soat7.myburguercontrol.domain.model.Order
import io.github.soat7.myburguercontrol.infrastructure.persistence.customer.repository.CustomerRepository
import io.github.soat7.myburguercontrol.infrastructure.persistence.order.repository.OrderRepository
import io.github.soat7.myburguercontrol.infrastructure.persistence.product.repository.ProductRepository
import org.springframework.stereotype.Component

@Component
class OrderDatabaseAdapter(
    private val repository: OrderRepository,
    private val customerRepository: CustomerRepository,
    private val productRepository: ProductRepository
) : OrderDatabasePort {

    override fun create(order: Order): Order = run {
        val customerEntity = customerRepository.findById(order.customer.id).get()
        repository.save(order.toPersistence(customerEntity) {
            productRepository.findById(it).get()
        }).toDomain()
    }

    override fun findByCustomer(customer: Customer): List<Order> = run {
        val customerEntity = customerRepository.findById(customer.id).get()
        repository.findByCustomer(customerEntity).map { it.toDomain() }
    }

}

