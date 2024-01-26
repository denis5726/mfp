package ru.mfp.stub

import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.mfp.payment.dto.CurrencyExchangeRatesDto
import ru.mfp.payment.dto.PaymentCreatingRequestDto
import ru.mfp.payment.dto.PaymentDto
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import kotlin.random.Random

@RestController
@RequestMapping("/stub")
@ConditionalOnProperty(value = ["mfp.stub.payment-service.enabled"], havingValue = "true")
class PaymentController {
    private val payments = mutableListOf<PaymentCreatingRequestDto>()
    private val data = mutableListOf<Account>()
    private val currencyExchangeRates = mutableMapOf<Currency, Map<Currency, BigDecimal>>()
    private val zero = BigDecimal.valueOf(0L)
    private val random = Random.Default

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
        data.add(
            Account(
                UUID.fromString("33333333-4444-5555-6666-777777777777"),
                BigDecimal.valueOf(0L),
                Currency.getInstance("USD")
            )
        )
        currencyExchangeRates[Currency.getInstance("RUB")] = mapOf(
            Pair(Currency.getInstance("USD"), BigDecimal("0.011367"))
        )
        currencyExchangeRates[Currency.getInstance("USD")] = mapOf(
            Pair(Currency.getInstance("RUB"), BigDecimal("87.97"))
        )
    }

    @PostMapping("/payments")
    fun createPayment(@RequestBody paymentCreatingRequestDto: PaymentCreatingRequestDto): PaymentDto {
        if (random.nextDouble() > 0.9) {
            return createResponse(paymentCreatingRequestDto, false, "Internal server error")
        }
        if (paymentCreatingRequestDto.createdAt.isBefore(LocalDateTime.now().minusMinutes(5))) {
            return createResponse(paymentCreatingRequestDto, false, "Request expired")
        }
        val amount = try {
            BigDecimal(paymentCreatingRequestDto.amount)
        } catch (e: IllegalArgumentException) {
            return createResponse(paymentCreatingRequestDto, false, "Invalid amount format")
        }
        if (amount <= zero) {
            return createResponse(paymentCreatingRequestDto, false, "Amount must be positive number")
        }
        val currency = try {
            Currency.getInstance(paymentCreatingRequestDto.currency)
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
        val accountFrom = data.first { it.id == paymentCreatingRequestDto.accountFrom }
        val accountTo = data.first { it.id == paymentCreatingRequestDto.accountTo }
        val amountFrom = if (accountFrom.currency == currency) {
            amount
        } else {
            currencyExchangeRates[currency]?.get(accountFrom.currency)?.multiply(amount)
                ?: return createResponse(
                    paymentCreatingRequestDto,
                    false,
                    "Exchanging not available for this currencies (from $currency to ${accountFrom.currency}"
                )
        }
        val amountTo = if (accountTo.currency == currency) {
            amount
        } else {
            currencyExchangeRates[currency]?.get(accountTo.currency)?.multiply(amount)
                ?: return createResponse(
                    paymentCreatingRequestDto,
                    false,
                    "Exchanging not available for this currencies (from $currency to ${accountTo.currency}"
                )
        }
        if (accountFrom.amount - amountFrom < zero) {
            return createResponse(paymentCreatingRequestDto, false, "AccountFrom doesn't have enough of money")
        }
        accountFrom.amount -= amountFrom
        accountTo.amount += amountTo
        payments.add(paymentCreatingRequestDto)
        return createResponse(paymentCreatingRequestDto, true, "Success")
    }

    @PostMapping("/accounts")
    fun addAccount(): UUID {
        val account = Account(UUID.randomUUID(), BigDecimal.valueOf(0L), Currency.getInstance("RUB"))
        data.add(account)
        return account.id
    }

    @GetMapping("/currencyExchangeRates/{currencyString}")
    fun findCurrencyExchangeRates(@PathVariable currencyString: String): ResponseEntity<CurrencyExchangeRatesDto> {
        val currency = try {
            Currency.getInstance(currencyString)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().build()
        }
        return ResponseEntity.ok(
            CurrencyExchangeRatesDto(
                currencyString,
                currencyExchangeRates.entries
                    .filter { it.key == currency }
                    .map { it.value }
                    .flatMap { it.entries }
                    .map { CurrencyExchangeRatesDto.ExchangeRate(it.key.currencyCode, it.value.toPlainString()) }
            )
        )
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
