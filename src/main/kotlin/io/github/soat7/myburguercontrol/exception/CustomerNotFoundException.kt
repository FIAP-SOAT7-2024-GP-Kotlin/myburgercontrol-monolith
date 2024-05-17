package io.github.soat7.myburguercontrol.exception

import io.github.soat7.myburguercontrol.domain.model.Customer

class CustomerNotFoundException(customer: Customer) : Exception("customer not found $customer")
