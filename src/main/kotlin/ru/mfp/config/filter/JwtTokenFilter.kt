package ru.mfp.config.filter

import ru.mfp.service.TokenProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtTokenFilter(private val tokenProvider: TokenProvider) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token: String? = tokenProvider.resolveToken(request)
        if (token != null && tokenProvider.isValidToken(token)) {
            SecurityContextHolder.getContext().authentication = tokenProvider.getAuthentication(token)
        }
        filterChain.doFilter(request, response)
    }
}
