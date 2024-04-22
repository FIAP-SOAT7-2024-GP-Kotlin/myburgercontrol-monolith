package io.github.soat7.myburguercontrol.repository

import io.github.soat7.myburguercontrol.entities.ClientEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ClientRepository : JpaRepository<ClientEntity, Long> {

}