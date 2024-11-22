package com.rooplor.classcraftbackend.repositories

import com.rooplor.classcraftbackend.entities.FormSubmission
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito.`when`
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.Optional
import kotlin.test.Test

@DataMongoTest
class FormSubmissionRepositoryTest {
    @MockBean
    private lateinit var formSubmissionRepository: FormSubmissionRepository

    @Test
    fun `should save and find form submission by id`() {
        val formSubmissionToSave =
            FormSubmission(
                id = "1",
                formId = "form1",
                classroomId = "class1",
                responses = mapOf("email" to "test@example.com", "phone" to "1234567890"),
            )
        `when`(formSubmissionRepository.save(formSubmissionToSave)).thenReturn(formSubmissionToSave)
        `when`(formSubmissionRepository.findById(formSubmissionToSave.id!!)).thenReturn(Optional.of(formSubmissionToSave))

        val savedFormSubmission = formSubmissionRepository.save(formSubmissionToSave)
        val foundFormSubmission = formSubmissionRepository.findById(savedFormSubmission.id!!).get()
        assertEquals(savedFormSubmission, foundFormSubmission)
    }

    @Test
    fun `should find all form submissions`() {
        val formSubmissions =
            listOf(
                FormSubmission(
                    id = "1",
                    formId = "form1",
                    classroomId = "class1",
                    responses = mapOf("email" to "test@example.com", "phone" to "1234567890"),
                ),
                FormSubmission(
                    id = "2",
                    formId = "form2",
                    classroomId = "class2",
                    responses = mapOf("address" to "123 Main St", "city" to "Anytown"),
                ),
            )
        `when`(formSubmissionRepository.findAll()).thenReturn(formSubmissions)

        val foundFormSubmissions = formSubmissionRepository.findAll()
        assertEquals(formSubmissions, foundFormSubmissions)
    }
}
