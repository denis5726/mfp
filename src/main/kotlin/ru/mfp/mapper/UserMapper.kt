package ru.mfp.mapper

import ru.mfp.dto.UserDto
import ru.mfp.dto.SignUpDto
import ru.mfp.entity.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface UserMapper {

    fun toDto(user: User) : UserDto

    @Mapping(target = "status", constant = "NEW")
    fun fromDto(signUpDto: SignUpDto) : User
}
