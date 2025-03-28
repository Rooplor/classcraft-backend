package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.configs.TestConfig
import com.rooplor.classcraftbackend.configs.TestSecurityConfig
import com.rooplor.classcraftbackend.dtos.Response
import com.rooplor.classcraftbackend.dtos.UserRequest
import com.rooplor.classcraftbackend.entities.User
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.services.AuthService
import com.rooplor.classcraftbackend.services.UserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(UserController::class)
@Import(TestSecurityConfig::class, TestConfig::class)
@ActiveProfiles("test")
class UserControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var modelMapper: ModelMapper

    @MockBean
    private lateinit var authService: AuthService

    @Test
    fun `should get all users`() {
        val users =
            listOf(
                User(id = "1", username = "user1", email = "user1@example.com", profilePicture = null),
                User(id = "2", username = "user2", email = "user2@example.com", profilePicture = null),
            )
        Mockito.`when`(userService.findAllUser()).thenReturn(users)

        mockMvc
            .perform(get("/api/user"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result").isArray)
            .andExpect(jsonPath("$.result[0].username").value("user1"))
            .andExpect(jsonPath("$.result[1].username").value("user2"))
    }

    @Test
    fun `should get user by id`() {
        val user = User(id = "1", username = "testuser", email = "test@example.com", profilePicture = null)
        Mockito.`when`(userService.findUserById("1")).thenReturn(user)

        mockMvc
            .perform(get("/api/user/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result.username").value("testuser"))
    }

    @Test
    fun `should create user`() {
        val userRequest = UserRequest(username = "newuser", email = "newuser@example.com")
        val user = User(id = "1", username = "newuser", email = "newuser@example.com", profilePicture = null)
        Mockito.`when`(modelMapper.map(userRequest, User::class.java)).thenReturn(user)
        Mockito.`when`(userService.createUser(user)).thenReturn(user)

        mockMvc
            .perform(
                post("/api/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                            "username": "newuser",
                            "email": "newuser@example.com"
                        }
                        """.trimIndent(),
                    ),
            ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result.username").value("newuser"))
    }

    @Test
    fun `should update user`() {
        val userRequest = UserRequest(username = "updateduser", email = "updated@example.com")
        val existingUser = User(id = "1", username = "existinguser", email = "existing@example.com", profilePicture = null)
        val updatedUser = User(id = "1", username = "updateduser", email = "updated@example.com", profilePicture = null)
        Mockito.`when`(userService.findUserById("1")).thenReturn(existingUser)
        Mockito.`when`(modelMapper.map(userRequest, User::class.java)).thenReturn(updatedUser)
        Mockito.`when`(userService.updateUser(existingUser, updatedUser)).thenReturn(updatedUser)

        mockMvc
            .perform(
                put("/api/user/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                            "username": "updateduser",
                            "email": "updated@example.com"
                        }
                        """.trimIndent(),
                    ),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result.username").value("updateduser"))
    }

    @Test
    fun `should delete user`() {
        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn("user2")
        Mockito
            .`when`(userService.findUserById("2"))
            .thenReturn(User(id = "1", username = "user1", email = "existing@example.com", profilePicture = null))
        Mockito.doNothing().`when`(userService).deleteUserById("1")

        mockMvc
            .perform(delete("/api/user/2"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result").value("User deleted"))
    }

    @Test
    fun `should not delete when user try to delete themselves`() {
        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn("user1")
        Mockito
            .`when`(userService.findUserById("1"))
            .thenReturn(User(id = "1", username = "user1", email = "existing@example.com", profilePicture = null))
        Mockito.doNothing().`when`(userService).deleteUserById("1")

        mockMvc
            .perform(delete("/api/user/1"))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.result").value(null))
            .andExpect(jsonPath("$.error").value(ErrorMessages.USER_CANNOT_DELETE_OWN_ACCOUNT))
    }

    @Test
    fun `getUserProfile should return user profile if authenticated`() {
        val username = "testUser"
        val user = User(username = username, email = "test@mail.com", profilePicture = "https://rooplor.com")

        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn(username)
        Mockito.`when`(userService.findByUsername(username)).thenReturn(user)

        val userController = UserController(userService, authService, mock())
        val response: ResponseEntity<Response<User>> = userController.getUserProfile()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(true, response.body?.success)
        assertEquals(user, response.body?.result)
        assertEquals(null, response.body?.error)
    }

    @Test
    fun `getUserProfile should return error if user not authenticated`() {
        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn(null)

        val userController = UserController(userService, authService, mock())
        val response: ResponseEntity<Response<User>> = userController.getUserProfile()

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals(false, response.body?.success)
        assertEquals(null, response.body?.result)
        assertEquals(ErrorMessages.USER_NOT_FOUND, response.body?.error)
    }

    @Test
    fun `getUserProfile should return error if exception occurs`() {
        val username = "testUser"

        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn(username)
        Mockito.`when`(userService.findByUsername(username)).thenThrow(RuntimeException("Database error"))

        val userController = UserController(userService, authService, mock())
        val response: ResponseEntity<Response<User>> = userController.getUserProfile()

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals(false, response.body?.success)
        assertEquals(null, response.body?.result)
        assertEquals("Database error", response.body?.error)
    }
}
