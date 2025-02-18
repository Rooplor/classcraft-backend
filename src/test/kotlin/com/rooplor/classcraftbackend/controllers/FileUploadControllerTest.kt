package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.configs.TestConfig
import com.rooplor.classcraftbackend.configs.TestSecurityConfig
import com.rooplor.classcraftbackend.services.FileUploadService
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.web.multipart.MultipartFile
import kotlin.test.Test

@WebMvcTest(FileUploadController::class)
@Import(TestSecurityConfig::class, TestConfig::class)
@ActiveProfiles("test")
class FileUploadControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var fileUploadService: FileUploadService

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
        `when`(fileUploadService.fileUpload(file, "classId")).thenReturn(response)

        mockMvc
            .perform(
                post("/api/file/upload")
                    .param("classId", "classId")
                    .param("file", "testfile.txt"),
            )
    }
}
