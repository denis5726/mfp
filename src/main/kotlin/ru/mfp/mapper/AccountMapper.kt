package ru.mfp.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.mfp.dto.AccountDto
import ru.mfp.entity.Account

@Mapper
interface AccountMapper {

    @Mapping(target = "user", source = "user.id")
    fun toDto(account: Account): AccountDto

    @Mapping(target = "user", source = "user.id")
    fun toDtoList(account: List<Account>): List<AccountDto>
}
