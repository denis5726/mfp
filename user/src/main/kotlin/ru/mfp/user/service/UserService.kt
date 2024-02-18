package ru.mfp.user.service

import java.util.UUID
import ru.mfp.user.dto.UserDto

interface UserService {

    fun findById(id: UUID): UserDto

    fun updateRole(id: UUID, newRole: String)
}
