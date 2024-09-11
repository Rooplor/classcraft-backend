package com.rooplor.classcraftbackend.services

import io.minio.*
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class FileUploadService(
    private val minioClient: MinioClient,
    private val environment: Environment,
) {
    fun fileUpload(
        file: MultipartFile,
        classId: String,
        className: String,
    ): String {
        val objectName = "${System.currentTimeMillis()}-${file.originalFilename}"
        val folder = "$classId-${className.toLowerCase()}/"

        val url = environment.getProperty("minio.url")
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

        return "$url:7000/api/v1/buckets/$bucketName/objects/download?preview=true&prefix=$folder$objectName"
    }
}
