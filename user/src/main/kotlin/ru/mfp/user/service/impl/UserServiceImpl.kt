package ru.mfp.user.service.impl

import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mfp.common.exception.ResourceNotFoundException
import ru.mfp.common.model.UserRole
import ru.mfp.user.dto.UserDto
import ru.mfp.user.mapper.UserMapper
import ru.mfp.user.repository.UserRepository
import ru.mfp.user.service.UserService

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) : UserService {

    override fun findById(id: UUID): UserDto =
        userMapper.toDto(findEntity(id))

    @Transactional
    override fun updateRole(id: UUID, newRole: String) {
        val user = findEntity(id)
        val role = UserRole.valueOf(newRole)
        if (!isValidTransition(user.role, role)) {
            throw IllegalArgumentException("Invalid role transition: ${user.role} to $role")
        }
        user.role = role
        userRepository.save(user)
    }

    private fun findEntity(id: UUID) = userRepository.findById(id)
        .orElseThrow { throw ResourceNotFoundException("User not found") }

    private fun isValidTransition(oldRole: UserRole, newRole: UserRole) =
        oldRole == UserRole.NEW && newRole == UserRole.EMAIL_VERIFIED
                || oldRole == UserRole.EMAIL_VERIFIED && newRole == UserRole.SOLVENCY_VERIFIED
                || oldRole == UserRole.NEW && newRole == UserRole.BANNED
                || oldRole == UserRole.EMAIL_VERIFIED && newRole == UserRole.BANNED
                || oldRole == UserRole.SOLVENCY_VERIFIED && newRole == UserRole.BANNED
}
