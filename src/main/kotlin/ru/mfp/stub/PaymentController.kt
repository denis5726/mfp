package ru.mfp.stub

import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.mfp.payment.dto.PaymentCreatingRequestDto
import ru.mfp.payment.dto.PaymentDto
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/stub")
@ConditionalOnProperty(value = ["mfp.stub.payment.enabled"], havingValue = "true")
class PaymentController {
    private val payments = mutableListOf<PaymentCreatingRequestDto>()
    private val data = mutableListOf<Account>()
    private val zero = BigDecimal.valueOf(0L)

    @PostConstruct
    fun init() {
        data.add(
            Account(
                UUID.fromString("11111111-2222-3333-4444-555555555555"),
                BigDecimal.valueOf(100000L),
                Currency.getInstance("RUB")
            )
        )
        data.add(
            Account(
                UUID.fromString("22222222-3333-4444-5555-666666666666"),
                BigDecimal.valueOf(1000L),
                Currency.getInstance("RUB")
            )
        )
    }

    @PostMapping("/payments")
    fun createPayment(@RequestBody paymentCreatingRequestDto: PaymentCreatingRequestDto): PaymentDto {
        if (paymentCreatingRequestDto.createdAt.isBefore(LocalDateTime.now().minusMinutes(5))) {
            return createResponse(paymentCreatingRequestDto, false, "Request expired")
        }
        val amount: BigDecimal?
        try {
            amount = BigDecimal(paymentCreatingRequestDto.amount)
        } catch (e: IllegalArgumentException) {
            return createResponse(paymentCreatingRequestDto, false, "Invalid amount format")
        }
        if (amount <= zero) {
            return createResponse(paymentCreatingRequestDto, false, "Amount must be positive number")
        }
        val currency: Currency
        try {
            currency = Currency.getInstance(paymentCreatingRequestDto.currency)
        } catch (e: IllegalArgumentException) {
            return createResponse(paymentCreatingRequestDto, false, "Invalid payment currency")
        }
        if (payments.stream().anyMatch { it.id == paymentCreatingRequestDto.id }) {
            return createResponse(paymentCreatingRequestDto, false, "Payment was already processed")
        }
        if (data.stream().noneMatch { it.id == paymentCreatingRequestDto.accountFrom }
            || data.stream().noneMatch { it.id == paymentCreatingRequestDto.accountTo }) {
            return createResponse(paymentCreatingRequestDto, false, "Account doesn't exist")
        }
        val accountFrom = data.stream().filter { it.id == paymentCreatingRequestDto.accountFrom }.findAny().orElseThrow()
        val accountTo = data.stream().filter { it.id == paymentCreatingRequestDto.accountTo }.findAny().orElseThrow()
        if (accountFrom.currency != accountTo.currency) {
            return createResponse(paymentCreatingRequestDto, false, "Accounts currency are not equal")
        }
        if (currency != accountFrom.currency) {
            return createResponse(paymentCreatingRequestDto, false, "Currency are not supported for this account")
        }
        if (accountFrom.amount - amount < zero) {
            return createResponse(paymentCreatingRequestDto, false, "AccountFrom doesn't have enough of money")
        }
        accountFrom.amount -= amount
        accountTo.amount += amount
        payments.add(paymentCreatingRequestDto)
        return createResponse(paymentCreatingRequestDto, true, "Success")
    }

    @PostMapping("/accounts")
    fun addAccount(): UUID {
        val account = Account(UUID.randomUUID(), BigDecimal.valueOf(0L), Currency.getInstance("RUB"))
        data.add(account)
        return account.id
    }

    private fun createResponse(
        paymentCreatingRequestDto: PaymentCreatingRequestDto,
        decision: Boolean,
        description: String
    ): PaymentDto {
        return PaymentDto(paymentCreatingRequestDto.id, UUID.randomUUID(), decision, description, LocalDateTime.now())
    }

    private data class Account(
        val id: UUID,
        var amount: BigDecimal,
        val currency: Currency
    )
}
