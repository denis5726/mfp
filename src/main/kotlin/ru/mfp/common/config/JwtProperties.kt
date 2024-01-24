package ru.mfp.common.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("mfp.jwt")
data class JwtProperties(
    var header: String?,
    var expiration: Long?,
    var secret: String?
)