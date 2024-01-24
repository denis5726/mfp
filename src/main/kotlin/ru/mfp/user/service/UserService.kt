package ru.mfp.user.service

import ru.mfp.user.dto.UserDto
import java.util.*

fun interface UserService {

    fun findById(id: UUID): UserDto
}
