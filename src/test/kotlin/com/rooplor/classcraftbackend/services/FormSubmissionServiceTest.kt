package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.dtos.UserDetailDTO
import com.rooplor.classcraftbackend.entities.Classroom
import com.rooplor.classcraftbackend.entities.Form
import com.rooplor.classcraftbackend.entities.FormField
import com.rooplor.classcraftbackend.entities.FormSubmission
import com.rooplor.classcraftbackend.entities.User
import com.rooplor.classcraftbackend.enums.AttendeesStatus
import com.rooplor.classcraftbackend.enums.ClassType
import com.rooplor.classcraftbackend.enums.FieldValidation
import com.rooplor.classcraftbackend.enums.Format
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.repositories.FormSubmissionRepository
import com.rooplor.classcraftbackend.services.mail.MailService
import com.rooplor.classcraftbackend.types.Attendees
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Optional

class FormSubmissionServiceTest {
    private val formSubmissionRepository = mock(FormSubmissionRepository::class.java)
    private val formService = mock(FormService::class.java)
    private val authService = mock(AuthService::class.java)
    private val userService = mock(UserService::class.java)
    private val classService = mock(ClassService::class.java)
    private val mailService = mock(MailService::class.java)
    private val formSubmissionService =
        FormSubmissionService(formSubmissionRepository, formService, authService, userService, classService, mailService)

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
        val classroom =
            Classroom(
                id = "class1",
                title = "React Native",
                details = "Learn how to build mobile apps with React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                isPublished = true,
                registrationStatus = true,
                dates = listOf(),
            )

        `when`(formService.findByClassroomId("class1")).thenReturn(form)
        `when`(formSubmissionRepository.save(formSubmission)).thenReturn(formSubmission)
        `when`(
            authService.getAuthenticatedUserDetails(),
        ).thenReturn(User(id = "owner1", username = "owner1", email = "owner1@mail.com", profilePicture = null))
        `when`(authService.getUserId()).thenReturn("owner1")
        `when`(userService.findUserById("owner1")).thenReturn(User(id = "owner1", username = "owner1", email = "owner1@mail.com"))
        `when`(classService.findClassById("class1")).thenReturn(classroom)

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
        `when`(authService.getUserId()).thenReturn("owner1")
        `when`(userService.findUserById("owner1")).thenReturn(User(id = "owner1", username = "owner1", email = "owner1@mail.com"))

        val exception = assertThrows<Exception> { formSubmissionService.submitForm(formSubmission) }

        assertEquals(ErrorMessages.MISSING_REQUIRED_FIELDS.replace("$0", "phone"), exception.message)
    }

    @Test
    fun `getFormSubmissionByFormIdAndSubmittedBy should return form submission`() {
        val formSubmission =
            FormSubmission(
                "1",
                "form1",
                "class1",
                mapOf("email" to "test@example.com"),
                mapOf(),
                "user1",
                UserDetailDTO("user1", "user1"),
            )
        `when`(formSubmissionRepository.findByFormIdAndSubmittedBy("form1", "user1")).thenReturn(formSubmission)

        val result = formSubmissionService.getFormSubmissionByFormIdAndSubmittedBy("form1", "user1")

        assertEquals(formSubmission, result)
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
                feedback =
                    listOf(
                        FormField(name = "feedback", type = "text", required = false, validation = null, options = null),
                    ),
            )
        val formSubmissions =
            listOf(
                FormSubmission(
                    id = "1",
                    formId = "form1",
                    classroomId = "class1",
                    responses = mapOf("email" to "test@example.com", "phone" to "1234567890"),
                    isApprovedByOwner = true,
                    attendeesStatus =
                        listOf(
                            Attendees(
                                day = 1,
                                date = LocalDate.of(2021, 9, 1),
                                attendeesStatus = AttendeesStatus.PRESENT,
                                checkInDateTime = LocalDateTime.of(2021, 9, 1, 9, 0),
                            ),
                        ),
                    feedbackResponse = mapOf("feedback" to "Great class!"),
                ),
            )

        `when`(formService.findByClassroomId("class1")).thenReturn(form)
        `when`(formSubmissionService.getFormSubmissionsByClassroomId("class1")).thenReturn(formSubmissions)

        val result = formSubmissionService.generateCsvFromForm("class1")

        val expectedCsv =
            "No.,email,phone,Registration Status,Attendees Status Day 1,Check-in at,feedback\n" +
                "1,test@example.com,1234567890,Approved,PRESENT,2021-09-01 09:00:00,Great class!\n"
        assertEquals(expectedCsv, result)
    }

    @Test
    fun `setFormSubmissionApprovalStatus should update form submission approval status`() {
        val formSubmission =
            FormSubmission(
                "1",
                "form1",
                "class1",
                mapOf("email" to "test@mail.com"),
                mapOf(),
                "user1",
                UserDetailDTO("user1", "user1"),
                false,
            )
        val expectation = formSubmission.copy(isApprovedByOwner = true)
        `when`(formSubmissionRepository.findById("1")).thenReturn(Optional.of(formSubmission))
        `when`(formSubmissionRepository.save(formSubmission)).thenReturn(expectation)
        `when`(classService.findClassById("class1")).thenReturn(Classroom(id = "class1", title = "React Native"))
        `when`(userService.findUserById("user1")).thenReturn(User(id = "user1", username = "user1", email = "123@gmail.com"))

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
        val formSubmission =
            FormSubmission(
                "1",
                "form1",
                "class1",
                mapOf("email" to "test@mail.com"),
                mapOf(),
                "user1",
                UserDetailDTO("user1", "user1"),
                true,
            )
        val expectation = formSubmission.copy(isApprovedByOwner = false)
        `when`(formSubmissionRepository.findById("1")).thenReturn(Optional.of(formSubmission))
        `when`(formSubmissionRepository.save(formSubmission)).thenReturn(expectation)
        `when`(classService.findClassById("class1")).thenReturn(Classroom(id = "class1", title = "React Native"))
        `when`(userService.findUserById("user1")).thenReturn(User(id = "user1", username = "user1", email = "123@gmail.com"))

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
                    userDetail = UserDetailDTO("user1", "user1"),
                ),
                FormSubmission(
                    id = "2",
                    formId = "form2",
                    classroomId = "class1",
                    responses = mapOf("phone" to "1234567890"),
                    submittedBy = "user1",
                    userDetail = UserDetailDTO("user1", "user1"),
                ),
            )

        `when`(formSubmissionRepository.findBySubmittedBy("user1")).thenReturn(formSubmissions)

        val result = formSubmissionService.getFormSubmissionByUserId("user1")

        assertEquals(formSubmissions, result)
    }

    @Test
    fun `setAttendeesStatus should update form submission attendees status`() {
        val formSubmission =
            FormSubmission(
                "1",
                "form1",
                "1",
                mapOf("email" to "test@mail.com"),
                mapOf(),
                "user1",
                UserDetailDTO("user1", "user1"),
                false,
            )
        val listOfAttendees = listOf(Attendees(1, LocalDate.now(), AttendeesStatus.PRESENT))
        val expectation = formSubmission.copy(attendeesStatus = listOfAttendees)
        `when`(formSubmissionRepository.findById("1")).thenReturn(Optional.of(formSubmission))
        `when`(formSubmissionRepository.save(formSubmission)).thenReturn(expectation)
        `when`(authService.getUserId()).thenReturn("user1")
        `when`(classService.findClassById("1")).thenReturn(Classroom(id = "1", title = "React Native"))
        `when`(userService.findUserById("user1")).thenReturn(User(id = "user1", username = "user1", email = "123@gmail.com"))

        val result = formSubmissionService.setAttendeesStatus("1", AttendeesStatus.PRESENT, 1)

        assertEquals(expectation, result)
        verify(formSubmissionRepository, times(1)).findById("1")
        verify(formSubmissionRepository, times(1)).save(formSubmission)
    }

    @Test
    fun `getUserInClassroom should return list of user detail DTO if isApprovedByOwner is true`() {
        val formSubmissions =
            listOf(
                FormSubmission(
                    id = "1",
                    formId = "form1",
                    classroomId = "class1",
                    responses = mapOf("email" to "test@mail.com"),
                    submittedBy = "user1",
                    userDetail = UserDetailDTO("user1", "user1"),
                    isApprovedByOwner = true,
                ),
                FormSubmission(
                    id = "2",
                    formId = "form2",
                    classroomId = "class1",
                    responses = mapOf("phone" to "1234567890"),
                    submittedBy = "user1",
                    userDetail = UserDetailDTO("user1", "user1"),
                    isApprovedByOwner = true,
                ),
            )

        `when`(formSubmissionService.getFormSubmissionsByClassroomId("class1")).thenReturn(formSubmissions)

        val result = formSubmissionService.getUserInClassroom("class1")

        assertEquals(formSubmissions.map { it.userDetail }, result)
    }

    @Test
    fun `getUserInClassroom should return empty list if isApprovedByOwner is false`() {
        val formSubmissions =
            listOf(
                FormSubmission(
                    id = "1",
                    formId = "form1",
                    classroomId = "class1",
                    responses = mapOf("email" to "test@mail.com"),
                    submittedBy = "user1",
                    userDetail = UserDetailDTO("user1", "user1"),
                    isApprovedByOwner = false,
                ),
                FormSubmission(
                    id = "2",
                    formId = "form2",
                    classroomId = "class1",
                    responses = mapOf("phone" to "1234567890"),
                    submittedBy = "user1",
                    userDetail = UserDetailDTO("user1", "user1"),
                    isApprovedByOwner = false,
                ),
            )

        `when`(formSubmissionService.getFormSubmissionsByClassroomId("class1")).thenReturn(formSubmissions)

        val result = formSubmissionService.getUserInClassroom("class1")

        assertEquals(emptyList<UserDetailDTO>(), result)
    }

    @Test
    fun `submitFeedback should save and return form submission`() {
        val formSubmissionId = "1"
        val feedbackResponse = mapOf("Full Name" to "John Doe", "Email" to "john.doe@example.com")
        val formSubmission =
            FormSubmission(
                id = formSubmissionId,
                formId = "form1",
                classroomId = "class1",
                responses = mapOf("email" to "test@example.com"),
                feedbackResponse = null,
            )
        val form =
            Form(
                id = "form1",
                classroomId = "class1",
                title = "Test Form",
                description = "Description",
                openDate = LocalDateTime.of(2024, 9, 1, 0, 0),
                closeDate = LocalDateTime.of(2024, 9, 30, 0, 0),
                fields = emptyList(),
                feedback =
                    listOf(
                        FormField(name = "Full Name", type = "text", required = true, validation = FieldValidation.TEXT),
                        FormField(name = "Email", type = "email", required = true, validation = FieldValidation.EMAIL),
                    ),
            )
        val updatedFormSubmission = formSubmission.copy(feedbackResponse = feedbackResponse)

        `when`(formSubmissionRepository.findById(formSubmissionId)).thenReturn(Optional.of(formSubmission))
        `when`(formService.findByClassroomId("class1")).thenReturn(form)
        `when`(formSubmissionRepository.save(formSubmission)).thenReturn(updatedFormSubmission)

        val result = formSubmissionService.submitFeedback(formSubmissionId, feedbackResponse)

        assertEquals(updatedFormSubmission, result)
        verify(formSubmissionRepository, times(1)).findById(formSubmissionId)
        verify(formService, times(1)).findByClassroomId("class1")
        verify(formSubmissionRepository, times(1)).save(formSubmission)
    }

    @Test
    fun `submitFeedback should throw exception on validation failure`() {
        val formSubmissionId = "1"
        val feedbackResponse = mapOf("Full Name" to "John Doe", "Email" to "invalid-email")
        val formSubmission =
            FormSubmission(
                id = formSubmissionId,
                formId = "form1",
                classroomId = "class1",
                responses = mapOf("email" to "test@example.com"),
                feedbackResponse = null,
            )
        val form =
            Form(
                id = "form1",
                classroomId = "class1",
                title = "Test Form",
                description = "Description",
                openDate = LocalDateTime.of(2024, 9, 1, 0, 0),
                closeDate = LocalDateTime.of(2024, 9, 30, 0, 0),
                fields = emptyList(),
                feedback =
                    listOf(
                        FormField(name = "Full Name", type = "text", required = true, validation = FieldValidation.TEXT),
                        FormField(name = "Email", type = "email", required = true, validation = FieldValidation.EMAIL),
                    ),
            )

        `when`(formSubmissionRepository.findById(formSubmissionId)).thenReturn(Optional.of(formSubmission))
        `when`(formService.findByClassroomId("class1")).thenReturn(form)

        val exception =
            assertThrows<Exception> {
                formSubmissionService.submitFeedback(formSubmissionId, feedbackResponse)
            }

        assertEquals(ErrorMessages.FIELD_VALIDATE_FAIL.replace("\$0", "Email"), exception.message)
        verify(formSubmissionRepository, times(1)).findById(formSubmissionId)
        verify(formService, times(1)).findByClassroomId("class1")
    }

    @Test
    fun `getFormSubmissionById should return form submission`() {
        val formSubmissionId = "1"
        val formSubmission =
            FormSubmission(
                id = formSubmissionId,
                formId = "form1",
                classroomId = "class1",
                responses = mapOf("email" to "mail@mail.com"),
            )

        `when`(formSubmissionRepository.findById(formSubmissionId)).thenReturn(Optional.of(formSubmission))

        val result = formSubmissionService.getFormSubmissionById(formSubmissionId)

        assertEquals(formSubmission, result)
        verify(formSubmissionRepository, times(1)).findById(formSubmissionId)
    }
}
