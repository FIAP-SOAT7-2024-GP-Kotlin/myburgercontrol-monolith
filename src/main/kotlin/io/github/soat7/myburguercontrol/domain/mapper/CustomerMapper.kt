package io.github.soat7.myburguercontrol.domain.mapper

import io.github.soat7.myburguercontrol.domain.model.Customer
import io.github.soat7.myburguercontrol.infrastructure.persistence.customer.entity.CustomerEntity
import io.github.soat7.myburguercontrol.infrastructure.rest.customer.api.request.CustomerCreationRequest
import io.github.soat7.myburguercontrol.infrastructure.rest.customer.api.response.CustomerResponse
import java.time.Instant
import java.util.UUID

fun CustomerCreationRequest.toDomain() = Customer(
    id = UUID.randomUUID(),
    cpf = this.cpf,
    name = this.name,
    email = this.email
)

fun CustomerEntity.toDomain() = Customer(
    id = this.id!!,
    cpf = this.cpf,
    name = this.name,
    email = this.email
)

fun Customer.toPersistence() = CustomerEntity(
    id = this.id,
    cpf = this.cpf,
    name = this.name,
    email = this.email,
    createdAt = Instant.now(),
    updatedAt = Instant.now()
)

fun Customer.toResponse() = CustomerResponse(
    id = this.id,
    cpf = this.cpf,
    name = this.name,
    email = this.email
)
