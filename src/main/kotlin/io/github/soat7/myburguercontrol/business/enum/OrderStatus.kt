package io.github.soat7.myburguercontrol.business.enum

import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

enum class OrderStatus {
    NEW,
    RECEIVED,
    IN_PROGRESS,
    READY,
    FINISHED,
    ;

    companion object {

        fun from(source: String): OrderStatus = try {
            OrderStatus.valueOf(source)
        } catch (ex: IllegalArgumentException) {
            logger.error(ex) { ex.message }
            throw ex
        }
    }
}
