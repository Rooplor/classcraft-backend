package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.dtos.Response
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.services.DashboardService
import com.rooplor.classcraftbackend.types.DashboardData
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dashboard")
class DashboardController(
    private val dashboardService: DashboardService,
) {
    @GetMapping("/{classroomId}")
    fun getDashboardData(
        @PathVariable classroomId: String,
    ): ResponseEntity<Response<DashboardData>> {
        try {
            val response = dashboardService.getDashboardData(classroomId)
            return ResponseEntity.ok(Response(success = true, result = response, error = null))
        } catch (e: Exception) {
            return if (e.message == ErrorMessages.CLASS_NOT_FOUND || e.message == ErrorMessages.VENUE_NOT_FOUND) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response(success = false, result = null, error = e.message))
            } else {
                ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
            }
        }
    }
}
