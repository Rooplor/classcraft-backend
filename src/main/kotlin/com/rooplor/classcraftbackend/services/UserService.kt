package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.entities.User
import com.rooplor.classcraftbackend.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    fun findAllUser(): List<User> = userRepository.findAll()

    fun findUserById(id: String): User = userRepository.findById(id).orElseThrow()

    fun insertUser(addedUser: User): User = userRepository.insert(addedUser)

    fun updateUser(user: User): User = userRepository.save(user)

    fun deleteUserById(id: String) = userRepository.deleteById(id)
}
