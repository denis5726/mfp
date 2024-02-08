package ru.mfp.account.mapper

import org.mapstruct.Mapper
import ru.mfp.account.dto.AccountDto
import ru.mfp.account.entity.Account

@Mapper
interface AccountMapper {

    fun toDto(account1: Account): AccountDto

    fun toDtoList(account1: List<Account>): List<AccountDto>
}
