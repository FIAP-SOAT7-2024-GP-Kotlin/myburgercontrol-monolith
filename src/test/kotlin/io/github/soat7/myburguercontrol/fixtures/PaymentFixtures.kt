package io.github.soat7.myburguercontrol.fixtures

import io.github.soat7.myburguercontrol.domain.dto.PaymentResult
import io.github.soat7.myburguercontrol.domain.enum.PaymentStatus
import io.github.soat7.myburguercontrol.domain.model.Payment
import java.math.BigDecimal
import java.util.UUID

object PaymentFixtures {
    fun mockPayment(): Payment {
        return Payment(
            id = UUID.randomUUID(),
            value = BigDecimal.valueOf(45.90),
            status = PaymentStatus.REQUESTED,
            authorizationId = null,
            customer = CustomerFixtures.mockDomainCustomer(UUID.randomUUID(), "12312312312")
        )
    }
}

object PaymentResultFixtures {
    fun mockPaymentResult(authorizationId: String?, approved: Boolean) =
        PaymentResult(authorizationId, approved)
}
