package ru.mfp.email.handler

import mu.KotlinLogging
import org.springframework.stereotype.Component
import ru.mfp.user.service.UserService
import ru.mfp.common.event.EventHandler
import ru.mfp.payment.dto.DepositEventDto
import ru.mfp.email.service.EmailService
import java.time.format.DateTimeFormatter

private val log = KotlinLogging.logger {  }

@Component
class EmailDepositEventHandler(
    private val emailService: EmailService,
    private val userService: UserService
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
        emailService.sendSimpleTextMessage(
            getUserEmail(event),
            depositCreatedMessageSubject,
            depositCreatedMessageTemplate.format(
                event.amount,
                event.currency,
                event.paymentId,
                event.eventTime.format(dateTimeFormatter)
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

    private fun getUserEmail(event: DepositEventDto) = userService.findById(event.userId).email
}
