package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.services.DashboardService
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
    ) = dashboardService.getDashboardData(classroomId)
}
