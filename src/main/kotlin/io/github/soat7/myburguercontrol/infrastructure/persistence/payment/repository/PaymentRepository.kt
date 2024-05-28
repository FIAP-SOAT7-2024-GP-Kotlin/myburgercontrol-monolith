package io.github.soat7.myburguercontrol.infrastructure.persistence.payment.repository

import io.github.soat7.myburguercontrol.infrastructure.persistence.payment.entity.PaymentEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PaymentRepository : JpaRepository<PaymentEntity, UUID>
