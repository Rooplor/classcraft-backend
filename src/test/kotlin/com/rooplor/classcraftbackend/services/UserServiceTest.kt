package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.entities.User
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.repositories.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.Optional
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@SpringBootTest
class UserServiceTest {
    @Autowired
    private lateinit var userService: UserService

    @MockBean
    private lateinit var userRepository: UserRepository

    @Test
    fun `should find all users`() {
        val users =
            listOf(
                User(id = "1", username = "user1", email = "user1@example.com", profilePicture = null),
                User(id = "2", username = "user2", email = "user2@example.com", profilePicture = null),
            )
        `when`(userRepository.findAll()).thenReturn(users)

        val foundUsers = userService.findAllUser()
        assertEquals(2, foundUsers.size)
        assertEquals("user1", foundUsers[0].username)
        assertEquals("user2", foundUsers[1].username)
    }

    @Test
    fun `should find user by id`() {
        val id = "1"
        val user = User(id = id, username = "testuser", email = "test@example.com", profilePicture = null)
        `when`(userRepository.findById(id)).thenReturn(Optional.of(user))

        val foundUser = userService.findUserById(id)
        assertEquals(id, foundUser.id)
    }

    @Test
    fun `should throw exception when user not found by id`() {
        val id = "notfound"
        `when`(userRepository.findById(id)).thenReturn(Optional.empty())

        val exception = assertThrows<Exception> { userService.findUserById(id) }
        assertEquals(ErrorMessages.USER_NOT_FOUND, exception.message)
    }

    @Test
    fun `should create user`() {
        val user = User(id = "1", username = "newuser", email = "newuser@example.com", profilePicture = null)
        `when`(userRepository.insert(user)).thenReturn(user)

        val createdUser = userService.createUser(user)
        assertEquals(user, createdUser)
    }

    @Test
    fun `should update user`() {
        val existingUser = User(id = "1", username = "existinguser", email = "existing@example.com", profilePicture = null)
        val updatedUser = User(id = "1", username = "updateduser", email = "updated@example.com", profilePicture = "newpic.jpg")
        `when`(userRepository.save(existingUser)).thenReturn(updatedUser)

        val result = userService.updateUser(existingUser, updatedUser)
        assertEquals(updatedUser.username, result.username)
        assertEquals(updatedUser.email, result.email)
        assertEquals(updatedUser.profilePicture, result.profilePicture)
    }

    @Test
    fun `should delete user by id`() {
        val id = "1"
        doNothing().`when`(userRepository).deleteById(id)

        userService.deleteUserById(id)
        verify(userRepository, times(1)).deleteById(id)
    }

    @Test
    fun `should throw exception when creating user with blank username`() {
        val user = User(id = "1", username = "", email = "newuser@example.com", profilePicture = null)
        val exception = assertThrows<Exception> { userService.createUser(user) }
        assertEquals(ErrorMessages.USER_USERNAME_CANNOT_BE_BLANK, exception.message)
    }

    @Test
    fun `should throw exception when creating user with existing username`() {
        val user = User(id = "1", username = "existinguser", email = "newuser@example.com", profilePicture = null)
        `when`(userRepository.findByUsername(user.username)).thenReturn(Optional.of(user))
        val exception = assertThrows<Exception> { userService.createUser(user) }
        assertEquals(ErrorMessages.USER_WITH_USERNAME_ALREADY_EXISTS.replace("\$0", user.username), exception.message)
    }

    @Test
    fun `should throw exception when creating user with blank email`() {
        val user = User(id = "1", username = "newuser", email = "", profilePicture = null)
        val exception = assertThrows<Exception> { userService.createUser(user) }
        assertEquals(ErrorMessages.USER_EMAIL_CANNOT_BE_BLANK, exception.message)
    }

    @Test
    fun `should throw exception when creating user with invalid email`() {
        val user = User(id = "1", username = "newuser", email = "invalid-email", profilePicture = null)
        val exception = assertThrows<Exception> { userService.createUser(user) }
        assertEquals(ErrorMessages.USER_EMAIL_INVALID, exception.message)
    }

    @Test
    fun `should throw exception when creating user with existing email`() {
        val user = User(id = "1", username = "newuser", email = "existing@example.com", profilePicture = null)
        `when`(userRepository.findByEmail(user.email)).thenReturn(Optional.of(user))
        val exception = assertThrows<Exception> { userService.createUser(user) }
        assertEquals(ErrorMessages.USER_WITH_EMAIL_ALREADY_EXISTS.replace("\$0", user.email), exception.message)
    }

    @Test
    fun `should throw exception when updating user with blank username`() {
        val existingUser = User(id = "1", username = "existinguser", email = "existing@example.com", profilePicture = null)
        val updatedUser = User(id = "1", username = "", email = "updated@example.com", profilePicture = "newpic.jpg")
        val exception = assertThrows<Exception> { userService.updateUser(existingUser, updatedUser) }
        assertEquals(ErrorMessages.USER_USERNAME_CANNOT_BE_BLANK, exception.message)
    }

    @Test
    fun `should throw exception when updating user with existing username`() {
        val existingUser = User(id = "1", username = "existinguser", email = "existing@example.com", profilePicture = null)
        val updatedUser = User(id = "1", username = "newuser", email = "updated@example.com", profilePicture = "newpic.jpg")
        `when`(userRepository.findByUsername(updatedUser.username)).thenReturn(Optional.of(updatedUser))
        val exception = assertThrows<Exception> { userService.updateUser(existingUser, updatedUser) }
        assertEquals(ErrorMessages.USER_WITH_USERNAME_ALREADY_EXISTS.replace("\$0", updatedUser.username), exception.message)
    }

    @Test
    fun `should throw exception when updating user with blank email`() {
        val existingUser = User(id = "1", username = "existinguser", email = "existing@example.com", profilePicture = null)
        val updatedUser = User(id = "1", username = "updateduser", email = "", profilePicture = "newpic.jpg")
        val exception = assertThrows<Exception> { userService.updateUser(existingUser, updatedUser) }
        assertEquals(ErrorMessages.USER_EMAIL_CANNOT_BE_BLANK, exception.message)
    }

    @Test
    fun `should throw exception when updating user with invalid email`() {
        val existingUser = User(id = "1", username = "existinguser", email = "existing@example.com", profilePicture = null)
        val updatedUser = User(id = "1", username = "updateduser", email = "invalid-email", profilePicture = "newpic.jpg")
        val exception = assertThrows<Exception> { userService.updateUser(existingUser, updatedUser) }
        assertEquals(ErrorMessages.USER_EMAIL_INVALID, exception.message)
    }

    @Test
    fun `should throw exception when updating user with existing email`() {
        val someUser = User(id = "1", username = "someuser", email = "existing@example.com", profilePicture = null)
        val existingUser = User(id = "2", username = "existinguser", email = "existing1@example.com", profilePicture = null)
        val updatedUser = User(id = "2", username = "updateduser", email = "existing@example.com", profilePicture = "newpic.jpg")
        `when`(userRepository.findByEmail(updatedUser.email)).thenReturn(Optional.of(someUser))
        val exception = assertThrows<Exception> { userService.updateUser(existingUser, updatedUser) }
        assertEquals(ErrorMessages.USER_WITH_EMAIL_ALREADY_EXISTS.replace("\$0", updatedUser.email), exception.message)
    }

    @Test
    fun `should find user by username`() {
        val username = "testuser"
        val user = User(id = "1", username = username, email = "testuser@example.com", profilePicture = null)
        `when`(userRepository.findByUsername(username)).thenReturn(Optional.of(user))

        val foundUser = userService.findByUsername(username)
        assertEquals(user, foundUser)
    }

    @Test
    fun `should throw exception when user not found by username`() {
        val username = "notfound"
        `when`(userRepository.findByUsername(username)).thenReturn(Optional.empty())

        val exception = assertThrows<Exception> { userService.findByUsername(username) }
        assertEquals(ErrorMessages.USER_NOT_FOUND, exception.message)
    }

    @Test
    fun `should return true if user exists by id`() {
        val id = "1"
        `when`(
            userRepository.findById(id),
        ).thenReturn(Optional.of(User(id = id, username = "testuser", email = "testuser@example.com", profilePicture = null)))

        val exists = userService.isUserExistById(id)
        assertTrue(exists)
    }

    @Test
    fun `should return false if user does not exist by id`() {
        val id = "notfound"
        `when`(userRepository.findById(id)).thenReturn(Optional.empty())

        val exists = userService.isUserExistById(id)
        assertFalse(exists)
    }
}
