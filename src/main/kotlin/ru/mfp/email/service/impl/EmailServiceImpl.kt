package ru.mfp.email.service.impl

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import ru.mfp.email.service.EmailService

@Service
class EmailServiceImpl(
    private val emailSender: JavaMailSender
) : EmailService {
    @Value("\${spring.mail.username}")
    private var from: String? = null

    override fun sendSimpleTextMessage(to: String, subject: String, text: String) {
        val message = SimpleMailMessage()
        message.from = from
        message.setTo(to)
        message.subject = subject
        message.text = text
        emailSender.send(message)
    }
}
