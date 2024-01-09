package ru.mfp.service

fun interface EmailService {

    fun sendSimpleTextMessage(to: String, subject: String, text: String)
}
