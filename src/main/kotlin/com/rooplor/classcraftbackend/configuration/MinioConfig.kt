package com.rooplor.classcraftbackend.configuration

import io.minio.MinioClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class MinioConfig(
    private val environment: Environment,
) {
    @Bean
    fun minioClient(): MinioClient {
        val url = environment.getProperty("minio.url")
        val port = environment.getProperty("minio.api-port")?.toInt()
        val accessKey = environment.getProperty("minio.access-key")
        val secretKey = environment.getProperty("minio.secret-key")

        return MinioClient
            .builder()
            .endpoint(url, port ?: 7001, false)
            .credentials(accessKey, secretKey)
            .build()
    }
}
