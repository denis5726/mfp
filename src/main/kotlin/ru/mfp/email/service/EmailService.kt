package ru.mfp.email.service

fun interface EmailService {

    fun sendSimpleTextMessage(to: String, subject: String, text: String)
}
