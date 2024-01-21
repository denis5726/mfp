package ru.mfp.email.handler

import mu.KotlinLogging
import org.springframework.stereotype.Component
import ru.mfp.auth.repository.UserRepository
import ru.mfp.common.event.EventHandler
import ru.mfp.common.exception.IllegalServerStateException
import ru.mfp.deposit.dto.DepositEventDto
import ru.mfp.deposit.service.DepositService
import ru.mfp.email.service.EmailService
import java.time.format.DateTimeFormatter

private val log = KotlinLogging.logger {  }

@Component
class DepositEventHandler(
    private val emailService: EmailService,
    private val depositService: DepositService,
    private val userRepository: UserRepository
) : EventHandler<DepositEventDto> {
    private val depositCreatedMessageSubject = "Ваш счёт был пополнен!"
    private val depositCreatedMessageTemplate =
        "Был осуществлён перевод на сумму %s%s.\nИдентификатор перевода: %s.\nВремя операции: %s."
    private val depositErrorMessageSubject = "Ошибка при переводе средств на счёт!"
    private val depositErrorMessageTemplate =
        "Ошибка при переводе от платёжного сервиса.\nСообщение: %s.\nИдентификатор перевода: %s.\nВремя операции: %s."
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-DD HH:mm:ss")

    override fun getEventClass(): Class<DepositEventDto> = DepositEventDto::class.java

    override fun handle(event: DepositEventDto) {
        log.info { "Sending a message by an event: $event" }
        if (event.decision) {
            sendSuccessDepositMessage(event)
            return
        }
        sendErrorDepositMessage(event)
    }

    private fun sendSuccessDepositMessage(event: DepositEventDto) {
        val deposit = getDeposit(event)
        emailService.sendSimpleTextMessage(
            getUserEmail(event),
            depositCreatedMessageSubject,
            depositCreatedMessageTemplate.format(
                deposit.amount,
                deposit.currency,
                deposit.paymentId,
                deposit.createdAt.format(dateTimeFormatter)
            )
        )
    }

    private fun sendErrorDepositMessage(event: DepositEventDto) {
        emailService.sendSimpleTextMessage(
            getUserEmail(event),
            depositErrorMessageSubject,
            depositErrorMessageTemplate.format(
                event.description,
                event.paymentId,
                event.eventTime.format(dateTimeFormatter)
            )
        )
    }

    private fun getUserEmail(event: DepositEventDto) = userRepository.findById(event.userId)
        .orElseThrow { throw IllegalServerStateException("User data not found in database") }
        .email

    private fun getDeposit(event: DepositEventDto) = depositService.findDepositByPaymentId(event.paymentId)
}
