package ru.mfp.mapper

import ru.mfp.dto.UserDto
import ru.mfp.dto.SignUpDto
import ru.mfp.entity.User
import org.mapstruct.Mapper

@Mapper
interface UserMapper {

    fun toDto(user: User) : UserDto

    fun fromDto(signUpDto: SignUpDto) : User
}
