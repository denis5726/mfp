package ru.mfp.email.service

fun interface EmailVerificationService {

    fun sendVerificationCode(email: String, code: String)
}
