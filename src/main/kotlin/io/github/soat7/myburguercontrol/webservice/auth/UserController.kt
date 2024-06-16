package io.github.soat7.myburguercontrol.webservice.auth

import io.github.soat7.myburguercontrol.business.mapper.toDomain
import io.github.soat7.myburguercontrol.business.mapper.toResponse
import io.github.soat7.myburguercontrol.business.service.UserService
import io.github.soat7.myburguercontrol.webservice.auth.api.UserCreationRequest
import io.github.soat7.myburguercontrol.webservice.auth.api.UserResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
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
@RequestMapping(
    path = ["/users"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class UserController(
    private val service: UserService,
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        tags = ["0 - Jornada de Autenticação"],
        summary = "Utilize esta rota para criar um novo usuário",
        description = "Utilize esta rota para criar um novo usuário",
    )
    fun createUser(@RequestBody request: UserCreationRequest): ResponseEntity<UserResponse> = run {
        val resp = service.create(request.toDomain())
        ResponseEntity.ok(resp.toResponse())
    }

    @GetMapping("/{id}")
    @Operation(
        tags = ["0 - Jornada de Autenticação"],
        summary = "Utilize esta rota para encontrar um usuário utilizando o identificador na base de dados",
        description = "Utilize esta rota para encontrar um usuário utilizando o identificador na base de dados",
    )
    @SecurityRequirement(name = "Bearer Authentication")
    fun findUserById(@PathVariable("id") id: UUID): ResponseEntity<UserResponse> = run {
        service.findUserById(id)?.let {
            ResponseEntity.ok().body(it.toResponse())
        } ?: ResponseEntity.notFound().build()
    }

    @GetMapping
    @Operation(
        tags = ["0 - Jornada de Autenticação"],
        summary = "Utilize esta rota para encontrar um usuário utilizando o cpf",
        description = "Utilize esta rota para encontrar um usuário utilizando o cpf",
    )
    @SecurityRequirement(name = "Bearer Authentication")
    fun findUserByCpf(@RequestParam("cpf") cpf: String): ResponseEntity<UserResponse> = run {
        service.findUserByCpf(cpf)?.let {
            ResponseEntity.ok(it.toResponse())
        } ?: ResponseEntity.notFound().build()
    }
}
