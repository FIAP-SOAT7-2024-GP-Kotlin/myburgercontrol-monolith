package io.github.soat7.myburguercontrol.config

import org.springframework.boot.context.properties.ConfigurationProperties

// Mapeamento do application.yaml
@ConfigurationProperties("jwt")
data class JwtProperties(
    val key: String,
    val accessTokenExpiration: Long,
    val refreshTokenExpiration: Long
)
