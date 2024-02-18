package ru.mfp.verification.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.ZonedDateTime
import java.util.UUID

@Entity
class EmailVerificationCode(
    @Id
    @Column(name = "email_verification_code_id")
    var id: UUID = UUID.randomUUID(),
    var userId: UUID,
    var value: String,
    var createdAt: ZonedDateTime = ZonedDateTime.now()
)
