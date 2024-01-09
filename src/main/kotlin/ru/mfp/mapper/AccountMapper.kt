package ru.mfp.mapper

import org.mapstruct.Mapper
import ru.mfp.dto.AccountDto
import ru.mfp.entity.Account

@Mapper
interface AccountMapper {

    fun toDto(account: Account): AccountDto

    fun toDtoList(account: List<Account>): List<AccountDto>

    fun fromDto(accountDto: AccountDto): Account
}
