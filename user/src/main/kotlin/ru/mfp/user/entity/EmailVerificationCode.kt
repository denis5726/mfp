package ru.mfp.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime
import java.util.UUID
import org.hibernate.annotations.CreationTimestamp

@Entity
class EmailVerificationCode(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "email_verification_code_id")
    var id: UUID,
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,
    var value: String,
    @CreationTimestamp
    var createdAt: LocalDateTime
)
