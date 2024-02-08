package ru.mfp.user.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.mfp.user.dto.SignUpDto
import ru.mfp.user.dto.UserDto
import ru.mfp.user.entity.User

@Mapper
interface UserMapper {

    fun toDto(user: User): UserDto

    @Mapping(target = "role", constant = "NEW")
    fun fromDto(signUpDto: SignUpDto): User
}
