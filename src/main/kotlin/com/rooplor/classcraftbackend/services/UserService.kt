package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.entities.User
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    fun findAllUser(): List<User> = userRepository.findAll()

    fun findUserById(id: String): User = userRepository.findById(id).orElseThrow({ Exception(ErrorMessages.USER_NOT_FOUND) })

    fun createUser(addedUser: User): User {
        validateCreateUser(addedUser)
        return userRepository.insert(addedUser)
    }

    fun updateUser(
        existingUser: User,
        updatedUser: User,
    ): User {
        validateUpdateUser(existingUser, updatedUser)
        existingUser.username = updatedUser.username
        existingUser.email = updatedUser.email
        existingUser.profilePicture = updatedUser.profilePicture
        return userRepository.save(existingUser)
    }

    fun deleteUserById(id: String) = userRepository.deleteById(id)

    fun isUserExistByEmail(email: String): Boolean = userRepository.findByEmail(email).isPresent

    fun isUserExistByUsername(username: String): Boolean = userRepository.findByUsername(username).isPresent

    private fun validateCreateUser(user: User) {
        val errorList = mutableListOf<String>()
        validateUsername(user.username, errorList)
        validateEmail(user.email, errorList)
        if (errorList.isNotEmpty()) {
            throw Exception(errorList.joinToString(", "))
        }
    }

    private fun validateUpdateUser(
        existingUser: User,
        updatedUser: User,
    ) {
        val errorList = mutableListOf<String>()
        if (existingUser.username != updatedUser.username) {
            validateUsername(updatedUser.username, errorList)
        }
        if (existingUser.email != updatedUser.email) {
            validateEmail(updatedUser.email, errorList)
        }
        if (errorList.isNotEmpty()) {
            throw Exception(errorList.joinToString(", "))
        }
    }

    private fun validateUsername(
        username: String,
        errorList: MutableList<String>,
    ) {
        if (username.isBlank()) {
            errorList.add(ErrorMessages.USER_USERNAME_CANNOT_BE_BLANK)
        } else if (isUserExistByUsername(username)) {
            errorList.add(ErrorMessages.USER_WITH_USERNAME_ALREADY_EXISTS.replace("\$0", username))
        }
    }

    private fun validateEmail(
        email: String,
        errorList: MutableList<String>,
    ) {
        if (email.isBlank()) {
            errorList.add(ErrorMessages.USER_EMAIL_CANNOT_BE_BLANK)
        } else {
            val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)\$".toRegex()
            if (!emailRegex.matches(email)) {
                errorList.add(ErrorMessages.USER_EMAIL_INVALID)
            }
            if (isUserExistByEmail(email)) {
                errorList.add(ErrorMessages.USER_WITH_EMAIL_ALREADY_EXISTS.replace("\$0", email))
            }
        }
    }
}
