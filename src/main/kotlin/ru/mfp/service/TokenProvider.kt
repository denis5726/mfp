package ru.mfp.service

import ru.mfp.model.JwtAuthentication
import jakarta.servlet.http.HttpServletRequest
import java.util.*

interface TokenProvider {

    fun isValidToken(token: String): Boolean

    fun getToken(jwtAuthentication: JwtAuthentication): String

    fun getAuthentication(token: String): JwtAuthentication

    fun resolveToken(request: HttpServletRequest): String?
}
