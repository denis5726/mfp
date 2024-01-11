package ru.mfp.service

import ru.mfp.dto.SignInDto
import ru.mfp.dto.SignUpDto
import ru.mfp.dto.TokenDto
import ru.mfp.dto.UserDto

interface AuthService {

    fun signUp(signUpDto: SignUpDto): UserDto

    fun signIn(signInDto: SignInDto): TokenDto
}
