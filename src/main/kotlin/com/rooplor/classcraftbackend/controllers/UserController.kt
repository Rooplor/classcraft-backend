package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.dtos.Response
import com.rooplor.classcraftbackend.dtos.UserRequest
import com.rooplor.classcraftbackend.entities.User
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.services.AuthService
import com.rooplor.classcraftbackend.services.UserService
import io.swagger.v3.oas.annotations.Operation
import org.modelmapper.ModelMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val authService: AuthService,
    private val modelMapper: ModelMapper,
) {
    @Operation(summary = "Get all users")
    @GetMapping
    fun getAllUsers(): ResponseEntity<Response<List<User>>> {
        try {
            val user = userService.findAllUser()
            return ResponseEntity.ok(Response(success = true, result = user, error = null))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
        }
    }

    @Operation(summary = "Get user profile")
    @GetMapping("/profile")
    fun getUserProfile(): ResponseEntity<Response<User>> {
        try {
            val username =
                authService.getAuthenticatedUser() ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Response(success = false, result = null, error = ErrorMessages.USER_NOT_FOUND),
                )
            val user = userService.findByUsername(username)
            return ResponseEntity.ok(Response(success = true, result = user, error = null))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
        }
    }

    @Operation(summary = "Get user by id")
    @GetMapping("/{id}")
    fun getUserById(
        @PathVariable id: String,
    ): ResponseEntity<Response<User>> {
        try {
            val user = userService.findUserById(id)
            return ResponseEntity.ok(Response(success = true, result = user, error = null))
        } catch (e: Exception) {
            return if (e.message == ErrorMessages.USER_NOT_FOUND) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response(success = false, result = null, error = e.message))
            } else {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }
        }
    }

    @Operation(summary = "Insert a new user")
    @PostMapping
    fun createUser(
        @RequestBody user: UserRequest,
    ): ResponseEntity<Response<User>> {
        try {
            val addedUser = userService.createUser(modelMapper.map(user, User::class.java))
            return ResponseEntity.status(HttpStatus.CREATED).body(Response(success = true, result = addedUser, error = null))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
        }
    }

    @Operation(summary = "Update user")
    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: String,
        @RequestBody userUpdate: UserRequest,
    ): ResponseEntity<Response<User>> {
        try {
            val existingUser = userService.findUserById(id)
            val updatedUser = modelMapper.map(userUpdate, User::class.java)
            val result = userService.updateUser(existingUser, updatedUser)
            return ResponseEntity.ok(Response(success = true, result = result, error = null))
        } catch (e: Exception) {
            return if (e.message == ErrorMessages.USER_NOT_FOUND) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response(success = false, result = null, error = e.message))
            } else {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }
        }
    }

    @Operation(summary = "Delete user by id")
    @DeleteMapping("/{id}")
    fun deleteUser(
        @PathVariable id: String,
    ): ResponseEntity<Response<String>> {
        try {
            val username = authService.getAuthenticatedUser()
            val user = userService.findUserById(id)
            if (user.username == username) {
                return ResponseEntity
                    .badRequest()
                    .body(Response(success = false, result = null, error = ErrorMessages.USER_CANNOT_DELETE_OWN_ACCOUNT))
            }
            userService.deleteUserById(id)
            return ResponseEntity.ok(Response(success = true, result = "User deleted", error = null))
        } catch (e: Exception) {
            return if (e.message == ErrorMessages.USER_NOT_FOUND) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response(success = false, result = null, error = e.message))
            } else {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }
        }
    }
}
