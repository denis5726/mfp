package ru.mfp.auth.service.impl

import org.springframework.stereotype.Service
import ru.mfp.auth.dto.UserDto
import ru.mfp.auth.mapper.UserMapper
import ru.mfp.auth.repository.UserRepository
import ru.mfp.auth.service.UserService
import ru.mfp.common.exception.ResourceNotFoundException
import java.util.*

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) : UserService {

    override fun findById(id: UUID): UserDto =
        userMapper.toDto(userRepository.findById(id).orElseThrow { throw ResourceNotFoundException("User not found") })
}
