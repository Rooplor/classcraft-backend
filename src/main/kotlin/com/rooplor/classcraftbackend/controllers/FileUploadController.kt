package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.dtos.FileResponse
import com.rooplor.classcraftbackend.dtos.Response
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
        @RequestParam("file") files: List<MultipartFile>,
        @Parameter(description = "File Category", required = true)
        @RequestParam("fileCategory") fileCategory: String,
    ): ResponseEntity<Response<FileResponse>> =
        try {
            val urls: MutableList<String> = ArrayList()
            files.map { file -> fileUploadService.fileUpload(file, fileCategory) }.map { url -> urls.add(url) }
            val result = FileResponse(urls)
            ResponseEntity.ok(Response(success = true, result = result, error = null))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(Response(success = false, result = null, error = e.message))
        }
}
