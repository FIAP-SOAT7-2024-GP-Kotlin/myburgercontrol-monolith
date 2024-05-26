package io.github.soat7.myburguercontrol.infrastructure.rest

import io.github.soat7.myburguercontrol.application.ports.inbound.UserServicePort
import io.github.soat7.myburguercontrol.domain.mapper.toDomain
import io.github.soat7.myburguercontrol.domain.mapper.toResponse
import io.github.soat7.myburguercontrol.infrastructure.rest.api.UserCreationRequest
import io.github.soat7.myburguercontrol.infrastructure.rest.api.UserResponse
import io.swagger.v3.oas.annotations.Operation
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

@RestController("user-controller")
@RequestMapping("/user")
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class UserController(
    private val service: UserServicePort
) {
    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        tags = ["0 - Jornada de Autenticação"],
        summary = "Utilize esta rota para criar um novo usuário",
        description = "Utilize esta rota para criar um novo usuário",
        operationId = "1"
    )
    fun createUser(@RequestBody request: UserCreationRequest): ResponseEntity<UserResponse> = run {
        val resp = service.create(request.toDomain())
        ResponseEntity.ok(resp.toResponse())
    }

    @GetMapping(
        path = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        tags = ["0 - Jornada de Autenticação"],
        summary = "Utilize esta rota para encontrar um usuário utilizando o identificador na base de dados",
        description = "Utilize esta rota para encontrar um usuário utilizando o identificador na base de dados",
        operationId = "2"
    )
    fun findUserById(@PathVariable("id") id: UUID): ResponseEntity<UserResponse> = run {
        service.findUserById(id)?.let {
            ResponseEntity.ok().body(it.toResponse())
        } ?: ResponseEntity.notFound().build()
    }

    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        tags = ["0 - Jornada de Autenticação"],
        summary = "Utilize esta rota para encontrar um usuário utilizando o cpf",
        description = "Utilize esta rota para encontrar um usuário utilizando o cpf",
        operationId = "3"
    )
    fun findUserByCpf(@RequestParam("cpf") cpf: String): ResponseEntity<UserResponse> = run {
        service.findUserByCpf(cpf)?.let {
            ResponseEntity.ok(it.toResponse())
        } ?: ResponseEntity.notFound().build()
    }
}
