package io.github.soat7.myburguercontrol.domain.model

import java.math.BigDecimal
import java.util.UUID

data class Payment(
    val id: UUID,
    var status: Status = Status.REQUESTED,
    val value: BigDecimal
) {
    enum class Status(){
        REQUESTED,
        APPROVED,
        DENIED
    }
}


