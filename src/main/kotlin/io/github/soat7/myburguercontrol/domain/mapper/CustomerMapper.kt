package io.github.soat7.myburguercontrol.domain.mapper

import io.github.soat7.myburguercontrol.domain.api.CustomerCreationRequest
import io.github.soat7.myburguercontrol.domain.api.CustomerResponse
import io.github.soat7.myburguercontrol.domain.entity.CustomerEntity

fun CustomerCreationRequest.toDomain() = CustomerEntity(
    cpf = this.cpf,
    name = this.name,
    email = this.email
)

fun CustomerEntity.toCustomerResponse() = CustomerResponse(
    id = this.id,
    cpf = this.cpf,
    name = this.name,
    email = this.email
)
