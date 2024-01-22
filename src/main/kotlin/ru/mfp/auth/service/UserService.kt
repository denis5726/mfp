package ru.mfp.auth.service

import ru.mfp.auth.dto.UserDto
import java.util.*

fun interface UserService {

    fun findById(id: UUID): UserDto
}
