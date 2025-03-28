package com.rooplor.classcraftbackend.services

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class AiService(
    private val classService: ClassService,
) {
    fun getAiResponse(classId: String): String {
        val client =
            OkHttpClient()
                .newBuilder()
                .connectTimeout(
                    15,
                    java.util.concurrent.TimeUnit.MINUTES,
                ).readTimeout(15, java.util.concurrent.TimeUnit.MINUTES)
                .writeTimeout(15, java.util.concurrent.TimeUnit.MINUTES)
                .build()
        val mediaType = "application/json".toMediaType()

        val classData = classService.findClassById(classId)

        val jsonBody =
            """
            {
              "model": "deepseek-r1:1.5b",
              "messages": [{"role": "user", "content": "Design ${classData.title} with detail: ${classData.details}, target:${classData.target}, target:${classData.prerequisite}. and with title: \"${classData.title}\" in ${classData.format} format and activityGuides and presentationGuides should be consistency"}],
              "stream": false,
              "format": {
                "type": "object",
                "properties": {
                  "title": { "type": "string" },
                  "content": { "type": "string" },
                  "activityGuides": {
                    "type": "array",
                    "items": {
                      "type": "object",
                      "properties": {
                        "topicName": { "type": "string" },
                        "activityGuide": { "type": "string" }
                      },
                      "required": ["topicName", "activityGuide"]
                    }
                  },
                  "presentationGuides": {
                    "type": "array",
                    "items": {
                      "type": "object",
                      "properties": {
                        "topicName": { "type": "string" },
                        "presentationGuide": { "type": "string" }
                      },
                      "required": ["topicName", "presentationGuide"]
                    }
                  }
                },
                "required": ["title", "content", "activityGuides", "presentationGuides"]
              }
            }
            """.trimIndent().toRequestBody(mediaType)

        val request =
            Request
                .Builder()
                .url("http://cp24kp2.sit.kmutt.ac.th:11434/api/chat")
                .post(jsonBody)
                .build()

        return try {
            val response = client.newCall(request).execute()
            response.body?.string() ?: "No response"
        } catch (e: IOException) {
            e.printStackTrace()
            "Error"
        }
    }
}
