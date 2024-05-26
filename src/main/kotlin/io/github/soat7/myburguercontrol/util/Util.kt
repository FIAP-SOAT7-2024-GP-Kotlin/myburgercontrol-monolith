package io.github.soat7.myburguercontrol.util

import java.math.BigDecimal

const val DEFAULT_BIG_DECIMAL_SCALE = 2

fun Double.toBigDecimal(): BigDecimal = BigDecimal(this.toString()).setScale(DEFAULT_BIG_DECIMAL_SCALE)
fun Long.toBigDecimal(): BigDecimal = BigDecimal(this.toString()).setScale(DEFAULT_BIG_DECIMAL_SCALE)
fun Int.toBigDecimal(): BigDecimal = BigDecimal(this.toString()).setScale(DEFAULT_BIG_DECIMAL_SCALE)
fun String.toBigDecimal(): BigDecimal = BigDecimal(this).setScale(DEFAULT_BIG_DECIMAL_SCALE)
