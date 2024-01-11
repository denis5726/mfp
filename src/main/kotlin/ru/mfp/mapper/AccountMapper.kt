package ru.mfp.mapper

import org.mapstruct.Mapper
import ru.mfp.dto.AccountDto
import ru.mfp.entity.Account

@Mapper
interface AccountMapper {

    fun toDto(account1: Account): AccountDto

    fun toDtoList(account1: List<Account>): List<AccountDto>
}
