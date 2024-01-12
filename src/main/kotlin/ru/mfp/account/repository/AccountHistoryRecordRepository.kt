package ru.mfp.account.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.mfp.account.entity.AccountHistoryRecord
import java.util.*

interface AccountHistoryRecordRepository : JpaRepository<AccountHistoryRecord, UUID>
