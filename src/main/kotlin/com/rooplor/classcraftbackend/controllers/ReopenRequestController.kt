package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.dtos.Response
import com.rooplor.classcraftbackend.entities.ReopenRequest
import com.rooplor.classcraftbackend.services.ReopenRequestService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/request")
class ReopenRequestController(
    private val reopenRequestService: ReopenRequestService,
) {
    @Operation(summary = "upsert a request")
    @GetMapping("/{classroomId}")
    fun upsertRequest(
        @PathVariable classroomId: String,
    ): ResponseEntity<Response<ReopenRequest>> {
        try {
            val request = reopenRequestService.upsertRequest(classroomId)
            return ResponseEntity.ok(Response(success = true, result = request, error = null))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
        }
    }

    @Operation(summary = "get requests by owner id")
    @GetMapping("/owner")
    fun getRequestByOwnerId(): ResponseEntity<Response<List<ReopenRequest>>> {
        try {
            val requests = reopenRequestService.getRequestByOwnerId()
            return ResponseEntity.ok(Response(success = true, result = requests, error = null))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
        }
    }

    @Operation(summary = "delete a request")
    @DeleteMapping("/{classroomId}")
    fun deleteRequest(
        @PathVariable classroomId: String,
    ): ResponseEntity<Response<String>> {
        try {
            reopenRequestService.deleteRequest(classroomId)
            return ResponseEntity.ok(Response(success = true, result = "Request deleted", error = null))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
        }
    }

    @Operation(summary = "Check if a request exists")
    @GetMapping("/exists/{classroomId}")
    fun requestExists(
        @PathVariable classroomId: String,
    ): ResponseEntity<Response<Boolean>> {
        try {
            val exists = reopenRequestService.requestExists(classroomId)
            return ResponseEntity.ok(Response(success = true, result = exists, error = null))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
        }
    }

    @Operation(summary = "Get My Requests")
    @GetMapping("/my-requests")
    fun getMyRequests(): ResponseEntity<Response<List<ReopenRequest>>> {
        try {
            val requests = reopenRequestService.getRequestByByUserId()
            return ResponseEntity.ok(Response(success = true, result = requests, error = null))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
        }
    }
}
