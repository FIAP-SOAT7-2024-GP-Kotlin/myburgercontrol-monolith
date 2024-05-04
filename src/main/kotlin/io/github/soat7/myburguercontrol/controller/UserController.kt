package io.github.soat7.myburguercontrol.controller

import io.github.soat7.myburguercontrol.dto.UserRequest
import io.github.soat7.myburguercontrol.dto.UserResponse
import io.github.soat7.myburguercontrol.entities.Role
import io.github.soat7.myburguercontrol.entities.User
import io.github.soat7.myburguercontrol.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@RestController("user-controller")
@RequestMapping("/user")
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class UserController(
    private val userService: UserService
) {
    @PostMapping()
    fun createUser(@RequestBody userRequest: UserRequest): UserResponse =
        userService.createUser(
            user = userRequest.toModel()
        )
            ?.toResponse()
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create user")

    @GetMapping
    fun listAll(): List<UserResponse> =
        userService.findByAll()
            .map { it.toResponse() }

    fun findByUUID(@PathVariable uuid: UUID): UserResponse {
        val user = userService.findByUUID(uuid) // Delega a busca ao UserService
            .orElseThrow { // Lança exceção se não encontrar
                ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find a id")
            }

        return UserResponse(user.uuid, user.email) // Retorna UserResponse
    }

    @DeleteMapping("/{uuid}")
    fun deleteUUID(@PathVariable uuid: UUID): ResponseEntity<Boolean> {
        val sucess = userService.deleteByUUID(uuid)

        return if (sucess) {
            ResponseEntity.noContent().build()
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find a user")
        }
    }

    private fun UserRequest.toModel() = User(
        uuid = UUID.randomUUID(),
        email = this.email,
        password = this.password,
        role = Role.USER
    )

    private fun User.toResponse(): UserResponse =
        UserResponse(
            uuid = this.uuid,
            email = this.email
        )
}
