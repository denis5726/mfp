package ru.mfp.user.service

import ru.mfp.user.dto.TokenDto
import ru.mfp.user.dto.UserDto
import ru.mfp.user.dto.SignInDto
import ru.mfp.user.dto.SignUpDto

interface AuthService {

    fun signUp(signUpDto: SignUpDto): UserDto

    fun signIn(signInDto: SignInDto): TokenDto
}
