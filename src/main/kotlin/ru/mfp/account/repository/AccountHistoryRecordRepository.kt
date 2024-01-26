package ru.mfp.account.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.mfp.account.entity.AccountHistoryRecord
import ru.mfp.account.model.AccountSnapshot
import java.time.LocalDateTime
import java.util.*

interface AccountHistoryRecordRepository : JpaRepository<AccountHistoryRecord, UUID> {

    @Query("""
        SELECT 
            a.account.id AS id, 
            SUM(a.diff) AS amount 
        FROM AccountHistoryRecord a 
        WHERE a.createdAt <= :time 
        GROUP BY a.account.id
    """)
    fun getSnapshots(time: LocalDateTime): List<AccountSnapshot>
}
