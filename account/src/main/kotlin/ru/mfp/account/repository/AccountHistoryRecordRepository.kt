package ru.mfp.account.repository

import java.time.ZonedDateTime
import java.util.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.mfp.account.entity.AccountHistoryRecord
import ru.mfp.account.model.AccountSnapshot

interface AccountHistoryRecordRepository : JpaRepository<AccountHistoryRecord, UUID> {

    @Query("""
        SELECT 
            a.account.id AS id, 
            SUM(a.diff) AS amount 
        FROM AccountHistoryRecord a 
        WHERE a.createdAt <= :time 
        GROUP BY a.account.id
    """)
    fun getSnapshots(time: ZonedDateTime): List<AccountSnapshot>
}
