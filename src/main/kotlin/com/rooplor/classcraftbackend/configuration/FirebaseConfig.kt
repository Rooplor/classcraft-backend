package com.rooplor.classcraftbackend.configuration

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.services.logger.LoggerService
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream

@Configuration
class FirebaseConfig(
    private val loggerService: LoggerService,
) {
    init {
        try {
            val serviceAccount = FileInputStream("src/main/resources/serviceAccountKey.json")

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
