package ru.mfp.auth.service.impl

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.mfp.auth.service.TokenProvider
import ru.mfp.common.exception.JwtInitializationException
import ru.mfp.common.model.JwtAuthentication
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

private val log = KotlinLogging.logger {}

@Component
class TokenProviderImpl : TokenProvider {
    @Value("\${jwt.header}")
    private var tokenHeader: String? = null

    @Value("\${jwt.expiration}")
    private var validityInSeconds: Long = 0

    @Value("\${jwt.secret}")
    private var secretKey: String? = null

    @PostConstruct
    fun init() {
        secretKey = if (secretKey != null) {
            Base64.getEncoder().encodeToString(secretKey!!.toByteArray(StandardCharsets.UTF_8))
        } else {
            throw JwtInitializationException("Secret key for jwt token is not found")
        }
    }

    override fun isValidToken(token: String): Boolean {
        return try {
            val claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
            !claimsJws.body.expiration.before(convertToDate(LocalDateTime.now()))
        } catch (e: JwtException) {
            log.error { "Jwt exception during checking validation of token: $token" }
            false
        } catch (e: IllegalArgumentException) {
            log.error("Exception during checking validation of token: {}", token)
            false
        }
    }

    override fun getToken(jwtAuthentication: JwtAuthentication): String {
        val claims = Jwts.claims().setSubject(jwtAuthentication.id.toString())
        val now = LocalDateTime.now()
        val validity = now.plus(validityInSeconds, ChronoUnit.SECONDS)
        return Jwts.builder()
            .addClaims(claims)
            .setIssuedAt(convertToDate(now))
            .setExpiration(convertToDate(validity))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    override fun getAuthentication(token: String): JwtAuthentication {
        val tokenBody = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body
        return JwtAuthentication(UUID.fromString(tokenBody.subject))
    }

    override fun resolveToken(request: HttpServletRequest): String? {
        return request.getHeader(tokenHeader)
    }

    private fun convertToDate(localDateTime: LocalDateTime): Date {
        return Date(Instant.from(localDateTime.atZone(ZoneId.of("UTC"))).toEpochMilli())
    }
}
