package com.rooplor.classcraftbackend.configuration

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.services.logger.LoggerService
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import java.io.FileInputStream

@Configuration
class FirebaseConfig(
    private val loggerService: LoggerService,
    private val environment: Environment,
) {
    init {
        try {
            val firebaseConfig =
                environment.getProperty("firebase.path.config")
                    ?: throw Exception(ErrorMessages.FIREBASE_CONFIG_NOT_FOUND)
            val serviceAccount = FileInputStream(firebaseConfig)

            val option =
                FirebaseOptions
                    .builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build()

            FirebaseApp.initializeApp(option)
        } catch (e: Exception) {
            loggerService.error(ErrorMessages.FIREBASE_CONFIG_ERROR + ": $e")
        }
    }
}
