package ru.mfp.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.mfp.dto.SignUpDto
import ru.mfp.dto.UserDto
import ru.mfp.entity.User

@Mapper
interface UserMapper {

    fun toDto(user: User): UserDto

    @Mapping(target = "status", constant = "NEW")
    fun fromDto(signUpDto: SignUpDto): User
}
