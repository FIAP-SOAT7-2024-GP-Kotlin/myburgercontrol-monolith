package io.github.soat7.myburguercontrol.domain.mapper

import io.github.soat7.myburguercontrol.domain.model.Customer
import io.github.soat7.myburguercontrol.infrastructure.outbound.customer.entity.CustomerEntity
import io.github.soat7.myburguercontrol.infrastructure.inbound.rest.api.CustomerCreationRequest
import io.github.soat7.myburguercontrol.infrastructure.inbound.rest.api.CustomerResponse
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
    email = this.email
)

fun Customer.toResponse() = CustomerResponse(
    id = this.id,
    cpf = this.cpf,
    name = this.name,
    email = this.email
)
