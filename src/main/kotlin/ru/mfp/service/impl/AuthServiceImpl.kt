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
    private val tokenProvider: TokenProvider,
    private val repository: UserRepository,
    private val mapper: UserMapper,
    private val passwordEncoder: PasswordEncoder,
    private val emptyLoginExceptionSupplier: () -> Nothing = {
        throw AuthorizationException(HttpStatus.BAD_REQUEST, "Login must not be null")
    }
) : AuthService {

    @Transactional
    override fun signUp(signUpDto: SignUpDto): UserDto {
        if (repository.existsByLogin(
                signUpDto.login ?: emptyLoginExceptionSupplier.invoke()
            )
        ) {
            log.error { "Attempt to create account with existent login: ${signUpDto.login}" }
            throw RegistrationException(HttpStatus.BAD_REQUEST, "User with login ${signUpDto.login} already exists")
        }
        log.info { "Saving new account with login: ${signUpDto.login}" }
        return mapper.toDto(repository.save(mapper.fromDto(signUpDto)))
    }

    override fun signIn(signInDto: SignInDto): TokenDto {
        val account = repository.findByLogin(
            signInDto.login ?: emptyLoginExceptionSupplier.invoke()
        )
            ?: throw AuthorizationException(HttpStatus.NOT_FOUND, "Account with login ${signInDto.login} is not found")
        if (!passwordEncoder.matches(signInDto.passwordHash?.let { CharBuffer.wrap(it) }, account.passwordHash)) {
            throw AuthorizationException(HttpStatus.FORBIDDEN, "Password verification failed")
        }
        if (account.id != null && account.login != null) {
            return TokenDto(tokenProvider.getToken(JwtAuthentication(account.id!!, account.login!!)))
        }
        throw IllegalServerStateException("User data not found in database")
    }
}
