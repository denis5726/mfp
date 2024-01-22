package ru.mfp.account.service.impl

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import ru.mfp.account.dto.AccountCreatingRequestDto
import ru.mfp.account.dto.AccountDto
import ru.mfp.account.entity.Account
import ru.mfp.account.exception.AccountCreatingException
import ru.mfp.account.mapper.AccountMapper
import ru.mfp.account.repository.AccountRepository
import ru.mfp.auth.entity.User
import ru.mfp.auth.entity.UserStatus
import ru.mfp.auth.repository.UserRepository
import ru.mfp.common.exception.IllegalServerStateException
import ru.mfp.common.model.JwtAuthentication
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class AccountServiceImplTest {
    private val accountRepository: AccountRepository = mock()
    private val accountMapper: AccountMapper = mock()
    private val userRepository: UserRepository = mock()
    private val accountArgumentCaptor = argumentCaptor<Account>()
    private val service: AccountServiceImpl = AccountServiceImpl(accountRepository, accountMapper, userRepository)

    @BeforeEach
    fun setUp() {
        ACCOUNT.id = UUID.randomUUID()
        ACCOUNT.user = User()
        ACCOUNT.amount = BigDecimal.valueOf(10)
        ACCOUNT.currency = Currency.getInstance("RUB")
        ACCOUNT.createdAt = LocalDateTime.now()

        USER.id = UUID.randomUUID()
        USER.email = "email"
        USER.passwordHash = "passwordHash"
        USER.status = UserStatus.SOLVENCY_VERIFIED
        USER.createdAt = LocalDateTime.now()
    }

    @Test
    fun findAccountsShouldReturnValidAccountList() {
        val entityList = listOf(ACCOUNT)
        val dtoList = listOf(ACCOUNT_DTO)
        whenever(accountRepository.findByUserIdOrderByCreatedAtDesc(AUTHENTICATION.id)).thenReturn(entityList)
        whenever(accountMapper.toDtoList(entityList)).thenReturn(dtoList)

        val result = service.findAccounts(AUTHENTICATION)

        assertThat(result).isEqualTo(dtoList)
    }

    @Test
    fun addAccountShouldThrowExceptionBecauseUserAlreadyHasAnAccount() {
        val userAccounts = listOf(ACCOUNT)
        whenever(accountRepository.findByUserIdOrderByCreatedAtDesc(AUTHENTICATION.id)).thenReturn(userAccounts)

        assertThatThrownBy { service.addAccount(ACCOUNT_CREATING_REQUEST, AUTHENTICATION) }.isInstanceOf(
            AccountCreatingException::class.java
        )
    }

    @Test
    fun addAccountShouldThrowExceptionBecauseUserNotFound() {
        val userAccounts = emptyList<Account>()
        whenever(accountRepository.findByUserIdOrderByCreatedAtDesc(AUTHENTICATION.id)).thenReturn(userAccounts)
        whenever(userRepository.findById(AUTHENTICATION.id)).thenReturn(Optional.empty())

        assertThatThrownBy { service.addAccount(ACCOUNT_CREATING_REQUEST, AUTHENTICATION) }.isInstanceOf(
            IllegalServerStateException::class.java
        )
    }

    @Test
    fun addAccountShouldThrowExceptionBecauseInvalidCurrencyProvided() {
        val userAccounts = emptyList<Account>()
        whenever(accountRepository.findByUserIdOrderByCreatedAtDesc(AUTHENTICATION.id)).thenReturn(userAccounts)
        whenever(userRepository.findById(AUTHENTICATION.id)).thenReturn(Optional.of(USER))

        assertThatThrownBy {
            service.addAccount(
                AccountCreatingRequestDto("invalidCurrency"),
                AUTHENTICATION
            )
        }.isInstanceOf(
            AccountCreatingException::class.java
        )
    }

    @Test
    fun addAccountShouldSuccessfullyAddAccount() {
        val userAccounts = emptyList<Account>()
        whenever(accountRepository.findByUserIdOrderByCreatedAtDesc(AUTHENTICATION.id)).thenReturn(userAccounts)
        whenever(userRepository.findById(AUTHENTICATION.id)).thenReturn(Optional.of(USER))
        doReturn(ACCOUNT).whenever(accountRepository).save(any())
        whenever(accountMapper.toDto(ACCOUNT)).thenReturn(ACCOUNT_DTO)

        val result = service.addAccount(ACCOUNT_CREATING_REQUEST, AUTHENTICATION)

        assertThat(result).isEqualTo(ACCOUNT_DTO)
        verify(accountRepository).save(accountArgumentCaptor.capture())

        val savedAccount = accountArgumentCaptor.firstValue

        assertAll(
            { assertThat(savedAccount.user).isEqualTo(USER) },
            { assertThat(savedAccount.amount).isEqualTo(BigDecimal.valueOf(0)) },
            { assertThat(savedAccount.currency).isEqualTo(Currency.getInstance(ACCOUNT_CREATING_REQUEST.currency)) },
        )
    }

    companion object {
        private val ACCOUNT = Account()
        private val ACCOUNT_DTO = AccountDto(UUID.randomUUID(), "100.00", "RUB")
        private val AUTHENTICATION = JwtAuthentication(UUID.randomUUID())
        private val ACCOUNT_CREATING_REQUEST = AccountCreatingRequestDto("USD")
        private val USER = User()
    }
}
