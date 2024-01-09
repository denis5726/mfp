package ru.mfp.service

import ru.mfp.dto.AccountDto
import ru.mfp.dto.SignInDto
import ru.mfp.dto.SignUpDto
import ru.mfp.dto.TokenDto

interface AuthService {

    fun signUp(signUpDto: SignUpDto): AccountDto

    fun signIn(signInDto: SignInDto): TokenDto
}
