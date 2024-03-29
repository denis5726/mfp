package ru.mfp.common.config.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import ru.mfp.common.config.security.TokenProvider

class JwtTokenFilter(
    private val tokenProvider: TokenProvider
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = tokenProvider.resolveToken(request)
        if (token != null && tokenProvider.isValidToken(token)) {
            SecurityContextHolder.getContext().authentication = tokenProvider.getAuthentication(token)
        }
        filterChain.doFilter(request, response)
    }
}
