package ru.mfp.account.mapper

import org.mapstruct.Mapper
import ru.mfp.account.dto.AccountDto
import ru.mfp.account.entity.Account

@Mapper
interface AccountMapper {

    fun toDto(account: Account): AccountDto

    fun toDtoList(account: List<Account>): List<AccountDto>
}
