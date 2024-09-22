package com.rooplor.classcraftbackend.repositories

import com.rooplor.classcraftbackend.entities.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.Optional

@DataMongoTest
class UserRepositoryTest {
    @MockBean
    private lateinit var userRepository: UserRepository

    @Test
    fun `should find user by email`() {
        val email = "test@example.com"
        val user = User(id = "1", username = "testuser", email = email, profilePicture = null)
        `when`(userRepository.findByEmail(email)).thenReturn(Optional.of(user))

        val foundUser = userRepository.findByEmail(email)
        assertTrue(foundUser.isPresent)
        assertEquals(email, foundUser.get().email)
    }

    @Test
    fun `should return empty when user not found by email`() {
        val email = "notfound@example.com"
        `when`(userRepository.findByEmail(email)).thenReturn(Optional.empty())

        val foundUser = userRepository.findByEmail(email)
        assertFalse(foundUser.isPresent)
    }

    @Test
    fun `should find user by username`() {
        val username = "testuser"
        val user = User(id = "1", username = username, email = "test@example.com", profilePicture = null)
        `when`(userRepository.findByUsername(username)).thenReturn(Optional.of(user))

        val foundUser = userRepository.findByUsername(username)
        assertTrue(foundUser.isPresent)
        assertEquals(username, foundUser.get().username)
    }

    @Test
    fun `should return empty when user not found by username`() {
        val username = "notfounduser"
        `when`(userRepository.findByUsername(username)).thenReturn(Optional.empty())

        val foundUser = userRepository.findByUsername(username)
        assertFalse(foundUser.isPresent)
    }

    @Test
    fun `should find all users`() {
        val users =
            listOf(
                User(id = "1", username = "user1", email = "user1@example.com", profilePicture = null),
                User(id = "2", username = "user2", email = "user2@example.com", profilePicture = null),
            )
        `when`(userRepository.findAll()).thenReturn(users)

        val foundUsers = userRepository.findAll()
        assertEquals(2, foundUsers.size)
        assertEquals("user1", foundUsers[0].username)
        assertEquals("user2", foundUsers[1].username)
    }

    @Test
    fun `should find user by id`() {
        val id = "1"
        val user = User(id = id, username = "testuser", email = "test@example.com", profilePicture = null)
        `when`(userRepository.findById(id)).thenReturn(Optional.of(user))

        val foundUser = userRepository.findById(id)
        assertTrue(foundUser.isPresent)
        assertEquals(id, foundUser.get().id)
    }

    @Test
    fun `should return empty when user not found by id`() {
        val id = "notfound"
        `when`(userRepository.findById(id)).thenReturn(Optional.empty())

        val foundUser = userRepository.findById(id)
        assertFalse(foundUser.isPresent)
    }
}
