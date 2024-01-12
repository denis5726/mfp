package ru.mfp.auth.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.mfp.auth.dto.SignUpDto
import ru.mfp.auth.dto.UserDto
import ru.mfp.auth.entity.User

@Mapper
interface UserMapper {

    fun toDto(user: User): UserDto

    @Mapping(target = "status", constant = "NEW")
    fun fromDto(signUpDto: SignUpDto): User
}
