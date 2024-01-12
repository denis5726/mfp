package ru.mfp.auth.rest

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.mfp.auth.service.AuthService
import ru.mfp.auth.dto.SignInDto
import ru.mfp.auth.dto.SignUpDto

@RestController
@RequestMapping("/auth")
class AuthController(val service: AuthService) {

    @PostMapping("/signUp")
    fun signUp(@RequestBody signUpDto: SignUpDto) = service.signUp(signUpDto)

    @PostMapping("/signIn")
    fun signIn(@RequestBody signInDto: SignInDto) = service.signIn(signInDto)
}
