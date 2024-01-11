package ru.mfp.service.impl

import mu.KotlinLogging
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mfp.dto.SignInDto
import ru.mfp.dto.SignUpDto
import ru.mfp.dto.TokenDto
import ru.mfp.dto.UserDto
import ru.mfp.exception.AuthorizationException
import ru.mfp.exception.RegistrationException
import ru.mfp.exception.ResourceNotFoundException
import ru.mfp.mapper.UserMapper
import ru.mfp.model.JwtAuthentication
import ru.mfp.repository.UserRepository
import ru.mfp.service.AuthService
import ru.mfp.service.TokenProvider
import java.nio.CharBuffer

private val log = KotlinLogging.logger { }

@Service
class AuthServiceImpl(
    private val tokenProvider: TokenProvider,
    private val repository: UserRepository,
    private val mapper: UserMapper,
    private val passwordEncoder: PasswordEncoder
) : AuthService {
    private val emailRegex: Regex = Regex(
        "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" +
                "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})\$"
    )

    @Transactional
    override fun signUp(signUpDto: SignUpDto): UserDto {
        if (repository.existsByEmail(signUpDto.email)) {
            log.error { "Attempt to create account with existent email: ${signUpDto.email}" }
            throw RegistrationException("User with email ${signUpDto.email} already exists")
        }
        if (!emailRegex.matches(signUpDto.email)) {
            log.error { "Provided invalid email address: ${signUpDto.email}" }
            throw RegistrationException("Invalid email address!")
        }
        log.info { "Saving new account with email: ${signUpDto.email}" }
        return mapper.toDto(repository.save(mapper.fromDto(signUpDto)))
    }

    override fun signIn(signInDto: SignInDto): TokenDto {
        val account = repository.findByEmail(signInDto.email)
            ?: throw ResourceNotFoundException("Account with email ${signInDto.email} is not found")
        if (!passwordEncoder.matches(CharBuffer.wrap(signInDto.passwordHash), account.passwordHash)) {
            throw AuthorizationException("Password verification failed")
        }
        return TokenDto(tokenProvider.getToken(JwtAuthentication(account.id)))
    }
}
