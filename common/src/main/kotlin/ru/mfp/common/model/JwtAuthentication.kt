package ru.mfp.common.model

import java.util.UUID
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class JwtAuthentication(
    val id: UUID,
    val mode: Mode,
    val role: UserRole
) : Authentication {

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return listOf(SimpleGrantedAuthority(role.name))
    }

    override fun getCredentials(): Any {
        throw UnsupportedOperationException("JwtAuthentication does not have credentials")
    }

    override fun getDetails(): Any {
        return id
    }

    override fun getPrincipal(): Any {
        return id
    }

    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        if (!isAuthenticated) {
            throw UnsupportedOperationException("JwtAuthentication is only for authenticated users")
        }
    }

    override fun getName(): String {
        return id.toString()
    }

    enum class Mode {
        USER, SERVICE
    }
}
