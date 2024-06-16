package io.github.soat7.myburguercontrol.webservice.order

import io.github.soat7.myburguercontrol.business.enum.OrderStatus
import io.github.soat7.myburguercontrol.business.mapper.toOrderDetails
import io.github.soat7.myburguercontrol.business.mapper.toResponse
import io.github.soat7.myburguercontrol.business.service.OrderService
import io.github.soat7.myburguercontrol.webservice.common.PaginatedResponse
import io.github.soat7.myburguercontrol.webservice.order.api.request.OrderCreationRequest
import io.github.soat7.myburguercontrol.webservice.order.api.response.OrderResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
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
import java.util.UUID

@RestController("order-controller")
@RequestMapping(
    path = ["orders"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@SecurityRequirement(name = "Bearer Authentication")
class OrderController(
    private val service: OrderService,
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        tags = ["2 - Jornada do Pedido"],
        summary = "Utilize esta rota para criar um pedido",
        description = "Utilize esta rota para criar um pedido",
    )
    fun create(@RequestBody request: OrderCreationRequest): ResponseEntity<OrderResponse> = run {
        val response = service.createOrder(request.toOrderDetails())
        ResponseEntity.ok(response.toResponse())
    }

    @GetMapping
    @Operation(
        tags = ["2 - Jornada do Pedido"],
        summary = "Utilize esta rota para encontrar o(s) pedido(s) por cpf de cliente",
        description = "Utilize esta rota para encontrar o(s) pedido(s) por cpf de cliente",
    )
    fun findOrdersByCpf(@RequestParam("cpf") cpf: String): ResponseEntity<List<OrderResponse>> = run {
        ResponseEntity.ok(service.findOrdersByCustomerCpf(cpf).map { it.toResponse() })
    }

    @GetMapping("/queue")
    @Operation(
        tags = ["2 - Jornada do Pedido"],
        summary = "Utilize esta rota para encontrar a lista do(s) pedido(s)",
        description = "Utilize esta rota para encontrar a lista do(s) pedido(s)",
    )
    fun findOrderQueue(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): ResponseEntity<PaginatedResponse<OrderResponse>> = run {
        val pageable = PageRequest.of(page, size)
        val response = service.findQueuedOrders(pageable)

        ResponseEntity.ok(
            PaginatedResponse(
                content = response.content.map { it.toResponse() },
                totalPages = response.totalPages,
                totalElements = response.totalElements,
                currentPage = response.number,
                pageSize = response.size,
            ),
        )
    }

    @PostMapping("/received", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        tags = ["2 - Jornada do Pedido"],
        summary = "Utilize esta rota para alterar o estado pedido para RECEBIDO",
        description = "Utilize esta rota para alterar o estado pedido para RECEBIDO",
    )
    fun updateOrderStatusToReceived(
        @RequestBody orderID: UUID,
    ) = run {
        ResponseEntity.ok(service.changeOrderStatus(OrderStatus.RECEIVED, orderID))
    }

    @PostMapping("/in-progress", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        tags = ["2 - Jornada do Pedido"],
        summary = "Utilize esta rota para alterar o estado pedido para EM PREPARAÇÃO",
        description = "Utilize esta rota para alterar o estado pedido para EM PREPARAÇÃO",
    )
    fun updateOrderStatusToInProgress(
        @RequestBody orderID: UUID,
    ) = run {
        ResponseEntity.ok(service.changeOrderStatus(OrderStatus.IN_PROGRESS, orderID))
    }

    @PostMapping("/ready", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        tags = ["2 - Jornada do Pedido"],
        summary = "Utilize esta rota para alterar o estado pedido para PRONTO",
        description = "Utilize esta rota para alterar o estado pedido para PRONTO",
    )
    fun updateOrderStatusToReady(
        @RequestBody orderID: UUID,
    ) = run {
        ResponseEntity.ok(service.changeOrderStatus(OrderStatus.READY, orderID))
    }

    @PostMapping("/finished", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        tags = ["2 - Jornada do Pedido"],
        summary = "Utilize esta rota para alterar o estado pedido para FINALIZADO",
        description = "Utilize esta rota para alterar o estado pedido para FINALIZADO",
    )
    fun updateOrderStatusToFinished(
        @RequestBody orderID: UUID,
    ) = run {
        ResponseEntity.ok(service.changeOrderStatus(OrderStatus.FINISHED, orderID))
    }
}
