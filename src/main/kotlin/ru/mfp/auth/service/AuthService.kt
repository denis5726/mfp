package ru.mfp.auth.service

import ru.mfp.auth.dto.TokenDto
import ru.mfp.auth.dto.UserDto
import ru.mfp.auth.dto.SignInDto
import ru.mfp.auth.dto.SignUpDto

interface AuthService {

    fun signUp(signUpDto: SignUpDto): UserDto

    fun signIn(signInDto: SignInDto): TokenDto
}
