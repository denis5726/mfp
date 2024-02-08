package ru.mfp.user.service.impl

import org.springframework.stereotype.Service
import ru.mfp.user.dto.UserDto
import ru.mfp.user.mapper.UserMapper
import ru.mfp.user.repository.UserRepository
import ru.mfp.user.service.UserService
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
