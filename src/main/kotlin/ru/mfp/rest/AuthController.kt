package ru.mfp.rest

import ru.mfp.dto.SignInDto
import ru.mfp.dto.SignUpDto
import ru.mfp.service.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(val service: AuthService) {

    @PostMapping("/signUp")
    fun signUp(@RequestBody signUpDto: SignUpDto) = service.signUp(signUpDto)

    @PostMapping("/signIn")
    fun signIn(@RequestBody signInDto: SignInDto) = service.signIn(signInDto)
}
