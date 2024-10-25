package com.rooplor.classcraftbackend.services

import io.minio.MinioClient
import io.minio.ObjectWriteResponse
import io.minio.PutObjectArgs
import io.minio.StatObjectArgs
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
import org.springframework.web.ErrorResponseException
import org.springframework.web.multipart.MultipartFile

@SpringBootTest
class FileUploadServiceTest {
    private val minioClient: MinioClient = mock(MinioClient::class.java)
    private val environment: Environment = mock(Environment::class.java)
    private val fileUploadService = FileUploadService(minioClient, environment)

    @BeforeEach
    fun setUp() {
        `when`(environment.getProperty("minio.bucket-name")).thenReturn("classcraft")
        `when`(environment.getProperty("minio.reverse-url")).thenReturn("http://localhost")
        `when`(environment.getProperty("minio.api-port")).thenReturn("80")
    }

    @Test
    fun `test file upload when folder does not exist`() {
        val file = mock(MultipartFile::class.java)
        `when`(file.originalFilename).thenReturn("testfile.txt")
        `when`(file.inputStream).thenReturn("test content".byteInputStream())
        `when`(file.size).thenReturn(12L)
        `when`(file.contentType).thenReturn("text/plain")

        doThrow(ErrorResponseException::class.java).`when`(minioClient).statObject(any(StatObjectArgs::class.java))
        doAnswer { mock(ObjectWriteResponse::class.java) }.`when`(minioClient).putObject(any(PutObjectArgs::class.java))

        val result = fileUploadService.fileUpload(file, "classId")

        verify(minioClient, times(2)).putObject(any(PutObjectArgs::class.java))
        assertTrue(
            result.matches(
                Regex(
                    "http://localhost/classcraft/classId/\\d+-testfile.txt",
                ),
            ),
        )
    }

    @Test
    fun `test file upload when folder exists`() {
        val file = mock(MultipartFile::class.java)
        `when`(file.originalFilename).thenReturn("testfile.txt")
        `when`(file.inputStream).thenReturn("test content".byteInputStream())
        `when`(file.size).thenReturn(12L)
        `when`(file.contentType).thenReturn("text/plain")

        doAnswer { invocation ->
            mock(ObjectWriteResponse::class.java)
        }.`when`(minioClient).putObject(any(PutObjectArgs::class.java))

        val result = fileUploadService.fileUpload(file, "classId")

        verify(minioClient, times(1)).putObject(any(PutObjectArgs::class.java))
        assertTrue(
            result.matches(
                Regex(
                    "http://localhost/classcraft/classId/\\d+-testfile.txt",
                ),
            ),
        )
    }
}
