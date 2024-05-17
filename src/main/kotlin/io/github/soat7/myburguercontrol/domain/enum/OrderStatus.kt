package io.github.soat7.myburguercontrol.domain.enum

import mu.KLogging

enum class OrderStatus {
    NEW;

    companion object : KLogging() {

        fun from(source: String): OrderStatus = try {
            OrderStatus.valueOf(source)
        } catch (ex: IllegalArgumentException) {
            logger.error(ex.message, ex)
            throw ex
        }

    }
}
