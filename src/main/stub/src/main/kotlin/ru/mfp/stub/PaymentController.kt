package ru.mfp.stub

import jakarta.annotation.PostConstruct
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.mfp.stub.dto.CreatedPaymentDto
import ru.mfp.stub.dto.PaymentDto
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/stub")
class PaymentController {
    private val payments = mutableListOf<CreatedPaymentDto>()
    private val data = mutableListOf<Account>()
    private val zero = BigDecimal.valueOf(0L)

    @PostConstruct
    fun init() {
        data.add(
            Account(
                UUID.fromString("11111111-2222-3333-444444444444"),
                BigDecimal.valueOf(100000L),
                Currency.getInstance("RUB")
            )
        )
    }

    @PostMapping("/payments")
    fun createPayment(@RequestBody createdPaymentDto: CreatedPaymentDto): PaymentDto {
        if (createdPaymentDto.createdAt.isBefore(LocalDateTime.now().minusMinutes(5))) {
            return createResponse(createdPaymentDto, false, "Request expired")
        }
        val amount: BigDecimal?
        try {
            amount = BigDecimal(createdPaymentDto.amount)
        } catch (e: IllegalArgumentException) {
            return createResponse(createdPaymentDto, false, "Invalid amount format")
        }
        if (amount <= zero) {
            return createResponse(createdPaymentDto, false, "Amount must be positive number")
        }
        if (payments.stream().anyMatch {it.id == createdPaymentDto.id}) {
            return createResponse(createdPaymentDto, false, "Payment was already processed")
        }
        if (data.stream().noneMatch { it.id == createdPaymentDto.accountFrom }
            || data.stream().noneMatch { it.id == createdPaymentDto.accountTo }) {
            return createResponse(createdPaymentDto, false, "Account doesn't exist")
        }
        val accountFrom = data.stream().filter { it.id == createdPaymentDto.accountFrom }.findAny().orElseThrow()
        val accountTo = data.stream().filter { it.id == createdPaymentDto.accountTo }.findAny().orElseThrow()
        if (accountFrom.currency != accountTo.currency) {
            return createResponse(createdPaymentDto, false, "Accounts currency are not equal")
        }
        if (accountFrom.amount - amount < zero) {
            return createResponse(createdPaymentDto, false, "AccountFrom doesn't have enough of money")
        }
        accountFrom.amount -= amount
        accountTo.amount += amount
        payments.add(createdPaymentDto)
        return createResponse(createdPaymentDto, true, "Success")
    }

    @PostMapping("/accounts")
    fun addAccount(): UUID {
        val account = Account(UUID.randomUUID(), BigDecimal.valueOf(0L), Currency.getInstance("RUB"))
        data.add(account)
        return account.id
    }

    private fun createResponse(createdPaymentDto: CreatedPaymentDto, decision: Boolean, description: String): PaymentDto {
        return PaymentDto(createdPaymentDto.id, UUID.randomUUID(), decision, description, LocalDateTime.now())
    }

    private data class Account(
        val id: UUID,
        var amount: BigDecimal,
        val currency: Currency
    )
}