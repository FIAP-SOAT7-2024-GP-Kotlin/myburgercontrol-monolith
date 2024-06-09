package io.github.soat7.myburguercontrol.database.payment.repository

import io.github.soat7.myburguercontrol.database.payment.entity.PaymentEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PaymentJpaRepository : JpaRepository<PaymentEntity, UUID>
