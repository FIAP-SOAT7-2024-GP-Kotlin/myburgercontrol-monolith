package io.github.soat7.myburguercontrol.fixtures

import io.github.soat7.myburguercontrol.business.enum.PaymentStatus
import io.github.soat7.myburguercontrol.business.model.Payment
import io.github.soat7.myburguercontrol.thirdparty.api.PaymentResult
import java.util.UUID

object PaymentFixtures {
    fun mockPayment(): Payment {
        return Payment(
            id = UUID.randomUUID(),
            status = PaymentStatus.REQUESTED,
            authorizationId = null,
        )
    }
}

object PaymentResultFixtures {
    fun mockPaymentResult(authorizationId: String?, approved: Boolean) =
        PaymentResult(authorizationId, approved)
}
