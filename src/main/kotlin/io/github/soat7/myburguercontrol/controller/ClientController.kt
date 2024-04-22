package io.github.soat7.myburguercontrol.controller

import io.github.soat7.myburguercontrol.dto.ClientDTO
import io.github.soat7.myburguercontrol.service.ClientService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

private val log = KotlinLogging.logger { }

@RestController("client-controller")
@RequestMapping(path = ["/client"])
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class ClientController @Autowired constructor(
    private val service: ClientService
) {

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createClient(@RequestBody clientDTO: ClientDTO) = run {
        val resp = service.create(clientDTO)
        ResponseEntity.ok(resp)
    }

    @GetMapping(
        path = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getClient(@PathVariable("id") id: Long) = run {
        val resp = service.getClient(id)
        ResponseEntity.ok(resp)
    }

    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getClientByCPF(@RequestParam("cpf") cpf: String) = run {
        val resp = service.getClientByCPF(cpf)
        ResponseEntity.ok(resp)
    }
}