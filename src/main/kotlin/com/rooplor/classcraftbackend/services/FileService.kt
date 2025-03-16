package com.rooplor.classcraftbackend.services

import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
import io.minio.StatObjectArgs
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class FileService(
    private val minioClient: MinioClient,
    private val environment: Environment,
) {
    fun fileUpload(
        file: MultipartFile,
        fileCategory: String,
    ): String {
        val objectName = "${System.currentTimeMillis()}-${file.originalFilename}"
        val folder = "$fileCategory/"

        val url = environment.getProperty("minio.reverse-url")
        val bucketName = environment.getProperty("minio.bucket-name")

        try {
            minioClient.statObject(
                StatObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .`object`(folder)
                    .build(),
            )
        } catch (e: Exception) {
            minioClient.putObject(
                PutObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .`object`(folder)
                    .stream("".byteInputStream(), 0, -1)
                    .build(),
            )
        }
        minioClient.putObject(
            PutObjectArgs
                .builder()
                .bucket(bucketName)
                .`object`("$folder$objectName")
                .stream(file.inputStream, file.size, -1)
                .contentType(file.contentType)
                .build(),
        )

        return "$url/$bucketName/$folder$objectName"
    }

    fun removeFile(fileUrl: String) {
        val url = environment.getProperty("minio.reverse-url")
        val bucketName = environment.getProperty("minio.bucket-name")

        val objectName = fileUrl.removePrefix("$url/$bucketName/")

        try {
            minioClient.removeObject(
                RemoveObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .`object`(objectName)
                    .build(),
            )
        } catch (e: Exception) {
            throw RuntimeException("Failed to remove file: $fileUrl", e)
        }
    }
}
