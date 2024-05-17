package io.github.soat7.myburguercontrol.infrastructure.rest.order

import io.github.soat7.myburguercontrol.application.ports.inbound.OrderServicePort
import io.github.soat7.myburguercontrol.domain.mapper.toResponse
import io.github.soat7.myburguercontrol.infrastructure.rest.customer.api.request.OrderCreationRequest
import io.github.soat7.myburguercontrol.infrastructure.rest.customer.api.response.OrderResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController("order-controller")
@RequestMapping(path = ["orders"])
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class OrderController(
    private val service: OrderServicePort
) {

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun create(@RequestBody request: OrderCreationRequest): ResponseEntity<OrderResponse> = run {
        val resp = service.newOrder(request.cpf)
        ResponseEntity.ok(resp.toResponse())
    }

    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun findOrderByCpf(@RequestParam("cpf") cpf: String): ResponseEntity<List<OrderResponse>> = run {
        ResponseEntity.ok(service.findOrders(cpf).map { it.toResponse() })
    }
}
