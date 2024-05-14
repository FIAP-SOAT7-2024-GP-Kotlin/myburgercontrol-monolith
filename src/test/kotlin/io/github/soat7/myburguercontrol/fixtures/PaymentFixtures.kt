package io.github.soat7.myburguercontrol.fixtures

import io.github.soat7.myburguercontrol.domain.model.Payment
import java.math.BigDecimal
import java.util.UUID

object PaymentFixtures {
    fun mockPayment(): Payment {

        return Payment(
            id = UUID.randomUUID(),
            value = BigDecimal.valueOf(45.90),
            status = Payment.Status.REQUESTED

        )
    }
}
