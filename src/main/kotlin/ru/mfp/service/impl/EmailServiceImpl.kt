package ru.mfp.service.impl

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import ru.mfp.service.EmailService

@Service
class EmailServiceImpl(
    @Value("\${spring.mail.username}")
    private var from: String? = null,
    private val emailSender: JavaMailSender
) : EmailService {

    override fun sendSimpleTextMessage(to: String, subject: String, text: String) {
        val message = SimpleMailMessage()
        message.from = from
        message.setTo(to)
        message.subject = subject
        message.text = text
        emailSender.send(message)
    }
}
