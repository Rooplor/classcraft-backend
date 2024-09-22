package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.dtos.UserRequest
import com.rooplor.classcraftbackend.entities.User
import com.rooplor.classcraftbackend.services.UserService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(UserController::class)
class UserControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var modelMapper: ModelMapper

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
            ).andExpect(status().isOk)
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
        Mockito.doNothing().`when`(userService).deleteUserById("1")

        mockMvc
            .perform(delete("/api/user/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result").value("User deleted"))
    }
}
