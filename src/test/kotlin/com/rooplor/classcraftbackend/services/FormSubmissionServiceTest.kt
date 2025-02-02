package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.entities.Form
import com.rooplor.classcraftbackend.entities.FormField
import com.rooplor.classcraftbackend.entities.FormSubmission
import com.rooplor.classcraftbackend.entities.User
import com.rooplor.classcraftbackend.enums.AttendeesStatus
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.repositories.FormSubmissionRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.time.LocalDateTime
import java.util.Optional

class FormSubmissionServiceTest {
    private val formSubmissionRepository = mock(FormSubmissionRepository::class.java)
    private val formService = mock(FormService::class.java)
    private val authService = mock(AuthService::class.java)
    private val formSubmissionService = FormSubmissionService(formSubmissionRepository, formService, authService)

    @Test
    fun `submitForm should save and return form submission`() {
        val form =
            Form(
                id = "1",
                classroomId = "class1",
                title = "Test Form 1",
                description = "Test form description",
                openDate = LocalDateTime.of(2021, 9, 1, 0, 0),
                closeDate = LocalDateTime.of(2021, 9, 30, 0, 0),
                fields =
                    listOf(
                        FormField(name = "email", type = "text", required = true, validation = null, options = null),
                        FormField(name = "phone", type = "text", required = false, validation = null, options = null),
                    ),
            )
        val formSubmission =
            FormSubmission(
                id = "1",
                formId = "form1",
                classroomId = "class1",
                responses = mapOf("email" to "test@example.com"),
            )

        `when`(formService.findByClassroomId("class1")).thenReturn(form)
        `when`(formSubmissionRepository.save(formSubmission)).thenReturn(formSubmission)
        `when`(
            authService.getAuthenticatedUserDetails(),
        ).thenReturn(User(id = "owner1", username = "owner1", email = "owner1@mail.com", profilePicture = null))

        val result = formSubmissionService.submitForm(formSubmission)

        assertEquals(formSubmission, result)
        verify(formSubmissionRepository, times(1)).save(formSubmission)
    }

    @Test
    fun `submitForm should throw exception when required fields are missing`() {
        val form =
            Form(
                id = "1",
                classroomId = "class1",
                title = "Test Form 1",
                description = "Test form description",
                openDate = LocalDateTime.of(2021, 9, 1, 0, 0),
                closeDate = LocalDateTime.of(2021, 9, 30, 0, 0),
                fields =
                    listOf(
                        FormField(name = "email", type = "text", required = true, validation = null, options = null),
                        FormField(name = "phone", type = "text", required = true, validation = null, options = null),
                    ),
            )
        val formSubmission =
            FormSubmission(
                id = "1",
                formId = "form1",
                classroomId = "class1",
                responses = mapOf("email" to "test@example.com"),
            )

        `when`(formService.findByClassroomId("class1")).thenReturn(form)
        `when`(
            authService.getAuthenticatedUserDetails(),
        ).thenReturn(User(id = "owner1", username = "owner1", email = "owner1@mail.com", profilePicture = null))

        val exception = assertThrows<Exception> { formSubmissionService.submitForm(formSubmission) }

        assertEquals(ErrorMessages.MISSING_REQUIRED_FIELDS.replace("$0", "phone"), exception.message)
    }

    @Test
    fun `getFormSubmissionByFormIdAndSubmittedBy should return form submission`() {
        val formSubmission = FormSubmission("1", "form1", "class1", mapOf("email" to "test@example.com"), "user1")
        `when`(formSubmissionRepository.findByFormIdAndSubmittedBy("form1", "user1")).thenReturn(formSubmission)

        val result = formSubmissionService.getFormSubmissionByFormIdAndSubmittedBy("form1", "user1")

        assertEquals(formSubmission, result)
    }

    @Test
    fun `getFormSubmissionByFormIdAndSubmittedBy should throw exception when submission not found`() {
        `when`(formSubmissionRepository.findByFormIdAndSubmittedBy("form1", "user1")).thenReturn(null)

        val exception =
            assertThrows<Exception> {
                formSubmissionService.getFormSubmissionByFormIdAndSubmittedBy("form1", "user1")
            }

        assertEquals(ErrorMessages.ANSWER_NOT_FOUND, exception.message)
    }

    @Test
    fun `getFormSubmissionsByClassroomId should return list of form submissions`() {
        val formSubmissions =
            listOf(
                FormSubmission(
                    id = "1",
                    formId = "form1",
                    classroomId = "class1",
                    responses = mapOf("email" to "test@example.com"),
                ),
                FormSubmission(
                    id = "2",
                    formId = "form2",
                    classroomId = "class1",
                    responses = mapOf("phone" to "1234567890"),
                ),
            )

        `when`(formSubmissionRepository.findByClassroomId("class1")).thenReturn(formSubmissions)

        val result = formSubmissionService.getFormSubmissionsByClassroomId("class1")

        assertEquals(formSubmissions, result)
        verify(formSubmissionRepository, times(1)).findByClassroomId("class1")
    }

    @Test
    fun `generateCsvFromForm should return CSV string`() {
        val form =
            Form(
                id = "1",
                classroomId = "class1",
                title = "Test Form 1",
                description = "Test form description",
                openDate = LocalDateTime.of(2021, 9, 1, 0, 0),
                closeDate = LocalDateTime.of(2021, 9, 30, 0, 0),
                fields =
                    listOf(
                        FormField(name = "email", type = "text", required = true, validation = null, options = null),
                        FormField(name = "phone", type = "text", required = false, validation = null, options = null),
                    ),
            )
        val formSubmissions =
            listOf(
                FormSubmission(
                    id = "1",
                    formId = "form1",
                    classroomId = "class1",
                    responses = mapOf("email" to "test@example.com", "phone" to "1234567890"),
                ),
            )

        `when`(formService.findByClassroomId("class1")).thenReturn(form)
        `when`(formSubmissionService.getFormSubmissionsByClassroomId("class1")).thenReturn(formSubmissions)

        val result = formSubmissionService.generateCsvFromForm("class1")

        val expectedCsv = "\"No.\",\"email\",\"phone\"\n\"1\",\"test@example.com\",\"1234567890\"\n"
        assertEquals(expectedCsv, result)
    }

    @Test
    fun `setFormSubmissionApprovalStatus should update form submission approval status`() {
        val formSubmission = FormSubmission("1", "form1", "class1", mapOf("email" to "test@mail.com"), "user1", false)
        val expectation = formSubmission.copy(isApprovedByOwner = true)
        `when`(formSubmissionRepository.findById("1")).thenReturn(Optional.of(formSubmission))
        `when`(formSubmissionRepository.save(formSubmission)).thenReturn(expectation)

        val result = formSubmissionService.setFormSubmissionApprovalStatus("1", true)

        assertEquals(expectation, result)
        verify(formSubmissionRepository, times(1)).findById("1")
        verify(formSubmissionRepository, times(1)).save(formSubmission)
    }

    @Test
    fun `setFormSubmissionApprovalStatus should throw exception when form submission not found`() {
        `when`(formSubmissionRepository.findById("1")).thenReturn(Optional.empty())

        val exception = assertThrows<Exception> { formSubmissionService.setFormSubmissionApprovalStatus("1", true) }

        assertEquals(ErrorMessages.ANSWER_NOT_FOUND, exception.message)
    }

    @Test
    fun `setFormSubmissionApprovalStatus should update form submission approval status to false`() {
        val formSubmission = FormSubmission("1", "form1", "class1", mapOf("email" to "test@mail.com"), "user1", true)
        val expectation = formSubmission.copy(isApprovedByOwner = false)
        `when`(formSubmissionRepository.findById("1")).thenReturn(Optional.of(formSubmission))
        `when`(formSubmissionRepository.save(formSubmission)).thenReturn(expectation)

        val result = formSubmissionService.setFormSubmissionApprovalStatus("1", false)

        assertEquals(expectation, result)
        verify(formSubmissionRepository, times(1)).findById("1")
        verify(formSubmissionRepository, times(1)).save(formSubmission)
    }

    @Test
    fun `getFormSubmissionByUserId should return list of form submissions`() {
        val formSubmissions =
            listOf(
                FormSubmission(
                    id = "1",
                    formId = "form1",
                    classroomId = "class1",
                    responses = mapOf("email" to "test@mail.com"),
                    submittedBy = "user1",
                ),
                FormSubmission(
                    id = "2",
                    formId = "form2",
                    classroomId = "class1",
                    responses = mapOf("phone" to "1234567890"),
                    submittedBy = "user1",
                ),
            )

        `when`(formSubmissionRepository.findBySubmittedBy("user1")).thenReturn(formSubmissions)

        val result = formSubmissionService.getFormSubmissionByUserId("user1")

        assertEquals(formSubmissions, result)
    }

    @Test
    fun `setAttendeesStatus should update form submission attendees status`() {
        val formSubmission = FormSubmission("1", "form1", "class1", mapOf("email" to "test@mail.com"), "user1", false)
        val expectation = formSubmission.copy(attendeesStatus = AttendeesStatus.PRESENT)
        `when`(formSubmissionRepository.findById("1")).thenReturn(Optional.of(formSubmission))
        `when`(formSubmissionRepository.save(formSubmission)).thenReturn(expectation)

        val result = formSubmissionService.setAttendeesStatus("1", AttendeesStatus.PRESENT)

        assertEquals(expectation, result)
        verify(formSubmissionRepository, times(1)).findById("1")
        verify(formSubmissionRepository, times(1)).save(formSubmission)
    }
}
