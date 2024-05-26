package io.github.soat7.myburguercontrol.infrastructure.rest.order

import io.github.soat7.myburguercontrol.application.ports.inbound.OrderServicePort
import io.github.soat7.myburguercontrol.domain.mapper.toOrderDetails
import io.github.soat7.myburguercontrol.domain.mapper.toResponse
import io.github.soat7.myburguercontrol.infrastructure.rest.common.PaginatedResponse
import io.github.soat7.myburguercontrol.infrastructure.rest.order.api.request.OrderCreationRequest
import io.github.soat7.myburguercontrol.infrastructure.rest.order.api.response.OrderResponse
import org.springframework.data.domain.PageRequest
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
@RequestMapping(
    path = ["orders"],
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class OrderController(
    private val service: OrderServicePort
) {

    @PostMapping
    fun create(@RequestBody request: OrderCreationRequest): ResponseEntity<OrderResponse> = run {
        val response = service.createOrder(request.toOrderDetails())
        ResponseEntity.ok(response.toResponse())
    }

    @GetMapping
    fun findOrdersByCpf(@RequestParam("cpf") cpf: String): ResponseEntity<List<OrderResponse>> = run {
        ResponseEntity.ok(service.findOrdersByCustomerCpf(cpf).map { it.toResponse() })
    }

    @GetMapping("/queue")
    fun findOrderQueue(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PaginatedResponse<OrderResponse>> = run {
        val pageable = PageRequest.of(page, size)
        val response = service.findQueuedOrders(pageable)

        ResponseEntity.ok(
            PaginatedResponse(
                content = response.content.map { it.toResponse() },
                totalPages = response.totalPages,
                totalElements = response.totalElements,
                currentPage = response.number,
                pageSize = response.size
            )
        )
    }
}
