package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.configs.TestConfig
import com.rooplor.classcraftbackend.configs.TestSecurityConfig
import com.rooplor.classcraftbackend.services.FileService
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.multipart.MultipartFile
import kotlin.test.Test

@WebMvcTest(FileController::class)
@Import(TestSecurityConfig::class, TestConfig::class)
@ActiveProfiles("test")
class FileControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var fileService: FileService

    @Test
    fun `test uploadFile`() {
        val file = mock(MultipartFile::class.java)
        `when`(file.originalFilename).thenReturn("testfile.txt")
        `when`(file.inputStream).thenReturn("test content".byteInputStream())
        `when`(file.size).thenReturn(12L)
        `when`(file.contentType).thenReturn("text/plain")
        val response =
            @Suppress("ktlint:standard:max-line-length")
            "http://localhost/classcraft/classId-classname/123-testfile.txt"
        `when`(fileService.fileUpload(file, "classId")).thenReturn(response)

        mockMvc
            .perform(
                post("/api/file/upload")
                    .param("classId", "classId")
                    .param("file", "testfile.txt"),
            )
    }

    @Test
    fun `test removeFile successfully`() {
        val fileUrl = "http://localhost/classcraft/classId/testfile.txt"

        mockMvc
            .perform(
                post("/api/file/remove")
                    .param("fileUrl", fileUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED),
            ).andExpect(status().isOk)
            .andExpect(content().json("""{"success":true,"result":null,"error":null}"""))

        verify(fileService).removeFile(fileUrl)
    }

    @Test
    fun `test removeFile failure`() {
        val fileUrl = "http://localhost/classcraft/classId/testfile.txt"
        val errorMessage = "Failed to remove file"

        doThrow(RuntimeException(errorMessage)).`when`(fileService).removeFile(fileUrl)

        mockMvc
            .perform(
                post("/api/file/remove")
                    .param("fileUrl", fileUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED),
            ).andExpect(status().isBadRequest)
            .andExpect(content().json("""{"success":false,"result":null,"error":"$errorMessage"}"""))

        verify(fileService).removeFile(fileUrl)
    }
}
