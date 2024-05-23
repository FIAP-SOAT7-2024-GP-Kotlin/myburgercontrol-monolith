package io.github.soat7.myburguercontrol.infrastructure.rest.auth

import io.github.soat7.myburguercontrol.application.ports.inbound.UserServicePort
import io.github.soat7.myburguercontrol.domain.mapper.toDomain
import io.github.soat7.myburguercontrol.domain.mapper.toResponse
import io.github.soat7.myburguercontrol.infrastructure.rest.auth.api.UserCreationRequest
import io.github.soat7.myburguercontrol.infrastructure.rest.auth.api.UserResponse
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
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class UserController(
    private val service: UserServicePort
) {

    @PostMapping
    fun createUser(@RequestBody request: UserCreationRequest): ResponseEntity<UserResponse> = run {
        val resp = service.create(request.toDomain())
        ResponseEntity.ok(resp.toResponse())
    }

    @GetMapping("/{id}")
    fun findUserById(@PathVariable("id") id: UUID): ResponseEntity<UserResponse> = run {
        service.findUserById(id)?.let {
            ResponseEntity.ok().body(it.toResponse())
        } ?: ResponseEntity.notFound().build()
    }

    @GetMapping
    fun findUserByCpf(@RequestParam("cpf") cpf: String): ResponseEntity<UserResponse> = run {
        service.findUserByCpf(cpf)?.let {
            ResponseEntity.ok(it.toResponse())
        } ?: ResponseEntity.notFound().build()
    }
}
