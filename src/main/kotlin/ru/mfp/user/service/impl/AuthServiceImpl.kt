package ru.mfp.user.service.impl

import mu.KotlinLogging
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mfp.common.config.TokenProvider
import ru.mfp.common.exception.ResourceNotFoundException
import ru.mfp.common.model.JwtAuthentication
import ru.mfp.user.dto.SignInDto
import ru.mfp.user.dto.SignUpDto
import ru.mfp.user.dto.TokenDto
import ru.mfp.user.dto.UserDto
import ru.mfp.common.model.UserStatus
import ru.mfp.user.exception.AuthorizationException
import ru.mfp.user.exception.RegistrationException
import ru.mfp.user.kafka.producer.AuthProducer
import ru.mfp.user.mapper.UserMapper
import ru.mfp.user.repository.UserRepository
import ru.mfp.user.service.AuthService
import java.nio.CharBuffer

private val log = KotlinLogging.logger { }

@Service
class AuthServiceImpl(
    private val tokenProvider: TokenProvider,
    private val repository: UserRepository,
    private val mapper: UserMapper,
    private val passwordEncoder: PasswordEncoder,
    private val authProducer: AuthProducer
) : AuthService {

    @Transactional
    override fun signUp(signUpDto: SignUpDto): UserDto {
        if (repository.existsByEmail(signUpDto.email)) {
            log.error { "Attempt to create account with existent email: ${signUpDto.email}" }
            throw RegistrationException("User with email ${signUpDto.email} already exists")
        }
        log.info { "Saving new account with email: ${signUpDto.email}" }
        val saved = repository.save(mapper.fromDto(signUpDto))
        authProducer.sendRegistrationEvent(saved)
        return mapper.toDto(saved)
    }

    override fun signIn(signInDto: SignInDto): TokenDto {
        val user = repository.findByEmail(signInDto.email)
            ?: throw ResourceNotFoundException("Account with email ${signInDto.email} is not found")
        if (user.status == UserStatus.BANNED) {
            throw AuthorizationException("You have been banned")
        }
        if (!passwordEncoder.matches(CharBuffer.wrap(signInDto.passwordHash), user.passwordHash)) {
            throw AuthorizationException("Password verification failed")
        }
        return TokenDto(
            tokenProvider.getToken(
                JwtAuthentication(
                    user.id,
                    JwtAuthentication.Mode.USER,
                    "USER",
                    user.status
                )
            )
        )
    }
}
