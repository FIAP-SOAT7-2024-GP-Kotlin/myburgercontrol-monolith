package io.github.soat7.myburguercontrol.application.rest

import io.github.soat7.myburguercontrol.domain.api.CustomerCreationRequest
import io.github.soat7.myburguercontrol.domain.api.CustomerResponse
import io.github.soat7.myburguercontrol.domain.mapper.toCustomerResponse
import io.github.soat7.myburguercontrol.domain.mapper.toDomain
import io.github.soat7.myburguercontrol.domain.ports.CustomerServicePort
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("customer-controller")
@RequestMapping(path = ["/customer"])
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class CustomerController(
    private val service: CustomerServicePort
) {

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createCustomer(@RequestBody request: CustomerCreationRequest): ResponseEntity<CustomerResponse> = run {
        val resp = service.create(request.toDomain())
        ResponseEntity.ok(resp.toCustomerResponse())
    }

    @GetMapping(
        path = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun findCustomerById(@PathVariable("id") id: UUID): ResponseEntity<CustomerResponse> = run {
        service.findCustomerById(id)?.let {
            ResponseEntity.ok().body(it.toCustomerResponse())
        } ?: ResponseEntity.notFound().build()
    }

    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun findCustomerByCpf(@RequestParam("cpf") cpf: String): ResponseEntity<CustomerResponse> = run {
        service.findCustomerByCpf(cpf)?.let {
            ResponseEntity.ok(it.toCustomerResponse())
        } ?: ResponseEntity.notFound().build()
    }
}
