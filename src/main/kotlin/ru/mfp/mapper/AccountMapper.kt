package ru.mfp.mapper

import ru.mfp.dto.AccountDto
import ru.mfp.dto.SignUpDto
import ru.mfp.entity.Account
import org.mapstruct.Mapper

@Mapper
interface AccountMapper {

    fun toDto(account: Account) : AccountDto

    fun fromDto(signUpDto: SignUpDto) : Account
}
