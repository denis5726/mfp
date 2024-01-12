package ru.mfp.email.handler

import org.springframework.stereotype.Component
import ru.mfp.auth.repository.UserRepository
import ru.mfp.common.event.EventHandler
import ru.mfp.common.exception.IllegalServerStateException
import ru.mfp.deposit.dto.CreatedDepositEventDto
import ru.mfp.email.service.EmailService

@Component
class DepositEventHandler(
    private val emailService: EmailService,
    private val userRepository: UserRepository
) : EventHandler<CreatedDepositEventDto> {
    private val depositCreatedMessageSubject = ""
    private val depositCreatedMessageTemplate = ""

    override fun getEventClass(): Class<CreatedDepositEventDto> = CreatedDepositEventDto::class.java

    override fun handle(event: CreatedDepositEventDto) {
        if (!event.decision) {
            return
        }
        val user = userRepository.findById(event.userId)
            .orElseThrow { throw IllegalServerStateException("User data not found in database") }
        emailService.sendSimpleTextMessage(user.email, depositCreatedMessageSubject, formatMessageTemplate(event))
    }

    private fun formatMessageTemplate(event: CreatedDepositEventDto): String {
        return depositCreatedMessageTemplate.format(event)
    }
}
