package ru.mfp.auth.service.impl

import mu.KotlinLogging
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mfp.auth.dto.TokenDto
import ru.mfp.auth.dto.UserDto
import ru.mfp.auth.mapper.UserMapper
import ru.mfp.auth.repository.UserRepository
import ru.mfp.auth.dto.SignInDto
import ru.mfp.auth.dto.SignUpDto
import ru.mfp.auth.exception.AuthorizationException
import ru.mfp.auth.exception.RegistrationException
import ru.mfp.auth.service.AuthService
import ru.mfp.auth.service.TokenProvider
import ru.mfp.common.exception.ResourceNotFoundException
import ru.mfp.common.model.JwtAuthentication
import java.nio.CharBuffer

private val log = KotlinLogging.logger { }

@Service
class AuthServiceImpl(
    private val tokenProvider: TokenProvider,
    private val repository: UserRepository,
    private val mapper: UserMapper,
    private val passwordEncoder: PasswordEncoder
) : AuthService {

    @Transactional
    override fun signUp(signUpDto: SignUpDto): UserDto {
        if (repository.existsByEmail(signUpDto.email)) {
            log.error { "Attempt to create account with existent email: ${signUpDto.email}" }
            throw RegistrationException("User with email ${signUpDto.email} already exists")
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
