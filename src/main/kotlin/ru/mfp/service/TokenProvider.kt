package ru.mfp.service

import jakarta.servlet.http.HttpServletRequest
import ru.mfp.model.JwtAuthentication

interface TokenProvider {

    fun isValidToken(token: String): Boolean

    fun getToken(jwtAuthentication: JwtAuthentication): String

    fun getAuthentication(token: String): JwtAuthentication

    fun resolveToken(request: HttpServletRequest): String?
}
