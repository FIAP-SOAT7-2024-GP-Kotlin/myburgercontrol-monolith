package io.github.soat7.myburguercontrol.service

import io.github.soat7.myburguercontrol.dto.ClientDTO
import io.github.soat7.myburguercontrol.entities.ClientEntity
import io.github.soat7.myburguercontrol.repository.ClientRepository
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ClientService @Autowired constructor(
    private val repository: ClientRepository
) {
    fun create(@Valid inputClientData: ClientDTO): ClientDTO {
        val saved = repository.save(ClientEntity(cpf = inputClientData.cpf))
        return saved.toDTO()
    }

    fun getClient(id: Long): ClientDTO? = repository.findByIdOrNull(id)?.toDTO()

    fun getClientByCPF(cpf: String): ClientDTO? = repository.findByCpf(cpf)?.toDTO()
}

fun ClientEntity.toDTO() = ClientDTO(
    id = this.id,
    cpf = this.cpf
)