package ru.mfp.model

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import java.util.*

data class JwtAuthentication(val id: UUID, val email: String) : Authentication {
    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return emptyList()
    }

    override fun getCredentials(): Any {
        throw UnsupportedOperationException("JwtAuthentication does not have credentials")
    }

    override fun getDetails(): Any {
        return email
    }

    override fun getPrincipal(): Any {
        return email
    }

    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        throw UnsupportedOperationException("JwtAuthentication is only for authenticated users")
    }

    override fun getName(): String {
        return email
    }
}
