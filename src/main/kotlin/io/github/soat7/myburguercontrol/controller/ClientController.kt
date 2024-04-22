package io.github.soat7.myburguercontrol.controller

import io.github.soat7.myburguercontrol.dto.ClientDTO
import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

private val log = KotlinLogging.logger { }

@RestController("client-controller")
@RequestMapping(path = ["/client"])
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class ClientController {

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createClient(@RequestBody clientDTO: ClientDTO) = run {
        log.debug { "client=$clientDTO" }
        val resp = ClientDTO(
            id = (1..1000).random().toLong(),
            cpf = clientDTO.cpf
        )
        log.debug { "resp=$resp" }
        ResponseEntity.ok(resp)
    }

    @GetMapping(
        path = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getClient(@PathVariable("id") id: Long) = run {
        log.debug { "id=$id" }
        val resp = ClientDTO(
            id = id,
            cpf = "aaa"
        )
        log.debug { "resp=$resp" }
        ResponseEntity.ok(resp)
    }
}