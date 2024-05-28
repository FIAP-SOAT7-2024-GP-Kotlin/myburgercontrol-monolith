package io.github.soat7.myburguercontrol.fixtures

import io.github.soat7.myburguercontrol.domain.dto.PaymentResult
import io.github.soat7.myburguercontrol.domain.enum.PaymentStatus
import io.github.soat7.myburguercontrol.domain.model.Payment
import java.util.UUID

object PaymentFixtures {
    fun mockPayment(): Payment {
        return Payment(
            id = UUID.randomUUID(),
            status = PaymentStatus.REQUESTED,
            authorizationId = null
        )
    }
}

object PaymentResultFixtures {
    fun mockPaymentResult(authorizationId: String?, approved: Boolean) =
        PaymentResult(authorizationId, approved)
}
