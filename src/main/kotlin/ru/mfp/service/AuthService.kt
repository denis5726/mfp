package ru.mfp.service

import ru.mfp.dto.UserDto
import ru.mfp.dto.SignInDto
import ru.mfp.dto.SignUpDto
import ru.mfp.dto.TokenDto

interface AuthService {

    fun signUp(signUpDto: SignUpDto): UserDto

    fun signIn(signInDto: SignInDto): TokenDto
}
