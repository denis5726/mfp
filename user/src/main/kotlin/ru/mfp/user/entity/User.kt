package ru.mfp.user.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*
import org.hibernate.annotations.CreationTimestamp
import ru.mfp.common.model.UserRole

@Entity
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    var id: UUID,
    var email: String,
    var passwordHash: String,
    @Enumerated(EnumType.STRING)
    var role: UserRole,
    @CreationTimestamp
    var createdAt: LocalDateTime
)