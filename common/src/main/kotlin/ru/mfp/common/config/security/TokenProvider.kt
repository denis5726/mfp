package ru.mfp.common.config.security

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Base64
import java.util.Date
import java.util.UUID
import mu.KotlinLogging
import ru.mfp.common.model.JwtAuthentication
import ru.mfp.common.model.UserRole

private val log = KotlinLogging.logger {}

class TokenProvider(
    private val properties: JwtProperties
) {
    private lateinit var encodedSecretKey: String
    private val modeKey = "mode"
    private val roleKey = "role"

    @PostConstruct
    fun init() {
        encodedSecretKey = Base64.getEncoder().encodeToString(properties.secret?.toByteArray(StandardCharsets.UTF_8))
    }

    fun isValidToken(token: String): Boolean {
        return try {
            val claimsJws = Jwts.parser().setSigningKey(encodedSecretKey).parseClaimsJws(token)
            !claimsJws.body.expiration.before(convertToDate(LocalDateTime.now()))
        } catch (e: JwtException) {
            log.error { "Jwt exception during checking validation of token: $token" }
            false
        } catch (e: IllegalArgumentException) {
            log.error("Exception during checking validation of token: {}", token)
            false
        }
    }

    fun getToken(jwtAuthentication: JwtAuthentication): String {
        val claims = Jwts.claims().setSubject(jwtAuthentication.id.toString())
        claims[modeKey] = jwtAuthentication.mode
        claims[roleKey] = jwtAuthentication.role
        val now = LocalDateTime.now()
        val validity = now.plus(properties.expiration ?: Long.MAX_VALUE, ChronoUnit.SECONDS)
        return Jwts.builder()
            .addClaims(claims)
            .setIssuedAt(convertToDate(now))
            .setExpiration(convertToDate(validity))
            .signWith(SignatureAlgorithm.HS256, encodedSecretKey)
            .compact()
    }

    fun getAuthentication(token: String): JwtAuthentication {
        val tokenBody = Jwts.parser()
            .setSigningKey(encodedSecretKey)
            .parseClaimsJws(token)
            .body
        return JwtAuthentication(
            UUID.fromString(tokenBody.subject),
            JwtAuthentication.Mode.valueOf(tokenBody[modeKey, String::class.java]),
            UserRole.valueOf(tokenBody[roleKey, String::class.java])
        )
    }

    fun resolveToken(request: HttpServletRequest): String? {
        return request.getHeader(properties.header)
    }

    private fun convertToDate(localDateTime: LocalDateTime): Date {
        return Date(Instant.from(localDateTime.atZone(ZoneId.of("UTC"))).toEpochMilli())
    }
}
