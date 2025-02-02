package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.configs.TestConfig
import com.rooplor.classcraftbackend.configs.TestSecurityConfig
import com.rooplor.classcraftbackend.dtos.FormCreateDTO
import com.rooplor.classcraftbackend.entities.Form
import com.rooplor.classcraftbackend.entities.FormSubmission
import com.rooplor.classcraftbackend.enums.AttendeesStatus
import com.rooplor.classcraftbackend.helpers.FormHelper
import com.rooplor.classcraftbackend.services.FormService
import com.rooplor.classcraftbackend.services.FormSubmissionService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.`when`
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@SpringBootTest
@Import(TestSecurityConfig::class, TestConfig::class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class FormControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var formService: FormService

    @MockBean
    private lateinit var formSubmissionService: FormSubmissionService

    @MockBean
    private lateinit var formHelper: FormHelper

    @MockBean
    private lateinit var modelMapper: ModelMapper

    @Test
    fun `createForm should return created form`() {
        val formCreateDTO =
            FormCreateDTO(
                "class1",
                "Test Form",
                "Description",
                LocalDateTime.of(2024, 9, 1, 0, 0),
                LocalDateTime.of(2024, 9, 30, 0, 0),
                emptyList(),
            )
        val form =
            Form(
                "1",
                "class1",
                "Test Form",
                "Description",
                LocalDateTime.of(2024, 9, 1, 0, 0),
                LocalDateTime.of(2024, 9, 30, 0, 0),
                emptyList(),
            )
        `when`(modelMapper.map(formCreateDTO, Form::class.java)).thenReturn(form)
        `when`(formService.createForm(form)).thenReturn(form)

        mockMvc
            .perform(
                post("/api/form")
                    .contentType("application/json")
                    .content(
                        """
                        {
                           "classroomId":"class1",
                           "title":"Test Form",
                           "description":"Description",
                           "openDate":"2024-09-01T00:00:00.0000Z",
                           "closeDate":"2024-09-30T00:00:00.0000Z",
                           "fields":[
                              
                           ]
                        }
                        """.trimIndent(),
                    ),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result.id").value("1"))
    }

    @Test
    fun `updateForm should return updated form`() {
        val formCreateDTO =
            FormCreateDTO(
                "class1",
                "Updated Form",
                "Updated Description",
                LocalDateTime.of(2024, 9, 1, 0, 0),
                LocalDateTime.of(2024, 9, 30, 0, 0),
                emptyList(),
            )
        val form =
            Form(
                "1",
                "class1",
                "Updated Form",
                "Updated Description",
                LocalDateTime.of(2024, 9, 1, 0, 0),
                LocalDateTime.of(2024, 9, 30, 0, 0),
                emptyList(),
            )
        `when`(modelMapper.map(formCreateDTO, Form::class.java)).thenReturn(form)
        `when`(formService.updateForm("1", form)).thenReturn(form)

        mockMvc
            .perform(
                put("/api/form/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                           "classroomId":"class1",
                           "title":"Updated Form",
                           "description":"Updated Description",
                           "openDate":"2024-09-01T00:00:00.0000Z",
                           "closeDate":"2024-09-30T00:00:00.0000Z",
                           "fields":[
                              
                           ]
                        }
                        """,
                    ),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result.id").value("1"))
    }

    @Test
    fun `getFormById should return form`() {
        val form =
            Form(
                "1",
                "class1",
                "Test Form",
                "Description",
                LocalDateTime.of(2024, 9, 1, 0, 0),
                LocalDateTime.of(2024, 9, 30, 0, 0),
                emptyList(),
            )
        `when`(formService.getFormById("1")).thenReturn(form)

        mockMvc
            .perform(get("/api/form/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result.id").value("1"))
    }

    @Test
    fun `deleteFormById should return success message`() {
        doNothing().`when`(formService).deleteFormById("1")

        mockMvc
            .perform(delete("/api/form/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result").value("Form deleted"))
    }

    @Test
    fun `submitForm should return submitted form`() {
        val formSubmission =
            FormSubmission("1", "form1", "class1", mapOf("email" to "test@example.com"), attendeesStatus = AttendeesStatus.PRESENT)
        `when`(formSubmissionService.submitForm(formSubmission)).thenReturn(formSubmission)

        mockMvc
            .perform(
                post("/api/form/submit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """{"id":"1","formId":"form1","classroomId":"class1","responses":{"email":"test@example.com"}, "attendeesStatus":"PRESENT"}""",
                    ),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result.id").value("1"))
    }

    @Test
    fun `generateCsvFromForm should return CSV file`() {
        val csvContent = "No.,email,phone\n1,test@example.com,1234567890\n"
        `when`(formSubmissionService.generateCsvFromForm("1")).thenReturn(csvContent)

        mockMvc
            .perform(get("/api/form/1/csv"))
            .andExpect(status().isOk)
            .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"form_1.csv\""))
            .andExpect(content().contentType("text/csv"))
            .andExpect(content().string(csvContent))
    }

    @Test
    fun `getFormSubmissions should return form submissions`() {
        val formSubmission = FormSubmission("1", "form1", "class1", mapOf("email" to "test@example.com"), "user1")
        `when`(formSubmissionService.getFormSubmissionByFormIdAndSubmittedBy("form1", "user1")).thenReturn(formSubmission)

        mockMvc
            .perform(
                get("/api/form/submissions")
                    .param("userId", "user1")
                    .param("formId", "form1")
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result.id").value("1"))
            .andExpect(jsonPath("$.result.formId").value("form1"))
            .andExpect(jsonPath("$.result.classroomId").value("class1"))
            .andExpect(jsonPath("$.result.responses.email").value("test@example.com"))
            .andExpect(jsonPath("$.result.submittedBy").value("user1"))
    }

    @Test
    fun `setFormApproval to true should return updated form`() {
        val formSubmission =
            FormSubmission(
                "1",
                "form1",
                "class1",
                mapOf("email" to "test@mail.com"),
                "user1",
                isApprovedByOwner = false,
            )
        `when`(formSubmissionService.setFormSubmissionApprovalStatus("1", true)).thenReturn(formSubmission)

        mockMvc
            .perform(
                patch("/api/form/isApprovedByOwner/1")
                    .param("isApproved", "true")
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result.id").value("1"))
    }

    @Test
    fun `setFormApproval to false should return updated form`() {
        val formSubmission =
            FormSubmission(
                "1",
                "form1",
                "class1",
                mapOf("email" to "test@mail.com"),
                "user1",
                isApprovedByOwner = true,
            )
        `when`(formSubmissionService.setFormSubmissionApprovalStatus("1", false)).thenReturn(formSubmission)

        mockMvc
            .perform(
                patch("/api/form/isApprovedByOwner/1")
                    .param("isApproved", "false")
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result.id").value("1"))
    }

    @Test
    fun `getFormSubmissionsByUserId should return form submissions`() {
        val formSubmission = FormSubmission("1", "form1", "class1", mapOf("email" to "test@mail.com"), "user1")
        `when`(formSubmissionService.getFormSubmissionByUserId("user1")).thenReturn(listOf(formSubmission))

        mockMvc
            .perform(
                get("/api/form/submissions/user/user1")
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result[0].id").value("1"))
            .andExpect(jsonPath("$.result[0].formId").value("form1"))
            .andExpect(jsonPath("$.result[0].classroomId").value("class1"))
            .andExpect(jsonPath("$.result[0].responses.email").value("test@mail.com"))
    }

    @Test
    fun `set attendees status should return updated form`() {
        val formSubmission =
            FormSubmission(
                "1",
                "form1",
                "class1",
                mapOf("email" to "test@mail.com"),
                "user1",
                isApprovedByOwner = true,
                attendeesStatus = AttendeesStatus.PRESENT,
            )

        `when`(formSubmissionService.setAttendeesStatus("1", AttendeesStatus.PRESENT)).thenReturn(formSubmission)

        mockMvc
            .perform(
                patch("/api/form/attendees/1")
                    .param("status", "PRESENT")
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result.id").value("1"))
    }
}
