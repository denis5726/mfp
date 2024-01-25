package ru.mfp.common.model

class RoleConstants {
    companion object {
        const val NEW = "hasAuthority('NEW')"
        const val EMAIL_VERIFIED = "hasAuthority('EMAIL_VERIFIED')"
        const val SOLVENCY_VERIFIED = "hasAuthority('SOLVENCY_VERIFIED')"
        const val BANNED = "hasAuthority('BANNED')"
        const val NOT_BANNED = "hasAnyAuthority('NEW', 'EMAIL_VERIFIED', 'SOLVENCY_VERIFIED')"
        const val EMAIL_VERIFIED_OR_HIGHER = "hasAuthority('EMAIL_VERIFIED', 'SOLVENCY_VERIFIED')"
    }
}