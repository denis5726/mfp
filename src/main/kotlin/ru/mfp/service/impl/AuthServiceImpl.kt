package ru.mfp.service.impl

import ru.mfp.dto.UserDto
import ru.mfp.dto.SignInDto
import ru.mfp.dto.SignUpDto
import ru.mfp.dto.TokenDto
import ru.mfp.exception.AuthorizationException
import ru.mfp.mapper.UserMapper
import ru.mfp.model.JwtAuthentication
import ru.mfp.repository.UserRepository
import ru.mfp.service.AuthService
import ru.mfp.service.TokenProvider
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mfp.exception.IllegalServerStateException
import ru.mfp.exception.RegistrationException
import java.nio.CharBuffer

private val log = KotlinLogging.logger { }

@Service
class AuthServiceImpl(
    private val emailRegex: Regex = Regex("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" +
            "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})\$"),
    private val emptyEmailExceptionSupplier: () -> Nothing = {
        throw AuthorizationException(HttpStatus.BAD_REQUEST, "Email must not be null")
    },
    private val tokenProvider: TokenProvider,
    private val repository: UserRepository,
    private val mapper: UserMapper,
    private val passwordEncoder: PasswordEncoder
) : AuthService {

    @Transactional
    override fun signUp(signUpDto: SignUpDto): UserDto {
        if (repository.existsByEmail(signUpDto.email)) {
            log.error { "Attempt to create account with existent email: ${signUpDto.email}" }
            throw RegistrationException(HttpStatus.BAD_REQUEST, "User with email ${signUpDto.email} already exists")
        }
        if (!emailRegex.matches(signUpDto.email)) {
            log.error { "Provided invalid email address: ${signUpDto.email}" }
            throw RegistrationException(HttpStatus.BAD_REQUEST, "Invalid email address!")
        }
        log.info { "Saving new account with email: ${signUpDto.email}" }
        return mapper.toDto(repository.save(mapper.fromDto(signUpDto)))
    }

    override fun signIn(signInDto: SignInDto): TokenDto {
        val account = repository.findByEmail(signInDto.email)
            ?: throw AuthorizationException(HttpStatus.NOT_FOUND, "Account with email ${signInDto.email} is not found")
        if (!passwordEncoder.matches(CharBuffer.wrap(signInDto.passwordHash), account.passwordHash)) {
            throw AuthorizationException(HttpStatus.FORBIDDEN, "Password verification failed")
        }
        if (account.id != null && account.email != null) {
            return TokenDto(tokenProvider.getToken(JwtAuthentication(account.id!!, account.email!!)))
        }
        throw IllegalServerStateException("User data not found in database")
    }
}
