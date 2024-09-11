package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.services.FileUploadService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/file")
class FileUploadController(
    private val fileUploadService: FileUploadService,
) {
    @Operation(
        summary = "Upload a file",
        description = "Uploads a file to the specified class bucket",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "File uploaded successfully"),
            ApiResponse(responseCode = "400", description = "Invalid input"),
        ],
    )
    @PostMapping("/upload", consumes = ["multipart/form-data"])
    fun uploadFile(
        @Parameter(description = "File to be uploaded", required = true, content = [Content(mediaType = "multipart/form-data")])
        @RequestParam("file") file: MultipartFile,
        @Parameter(description = "Class ID", required = true)
        @RequestParam("classId") classId: String,
        @Parameter(description = "Class Name", required = true)
        @RequestParam("className") className: String,
    ): ResponseEntity<String> =
        try {
            ResponseEntity.ok(fileUploadService.fileUpload(file, classId, className))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
}
