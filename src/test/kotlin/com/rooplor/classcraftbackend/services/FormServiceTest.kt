package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.entities.Form
import com.rooplor.classcraftbackend.entities.FormField
import com.rooplor.classcraftbackend.helpers.FormHelper
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.repositories.FormRepository
import com.rooplor.classcraftbackend.repositories.FormSubmissionRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.time.LocalDateTime
import java.util.Optional

class FormServiceTest {
    private val formRepository = mock(FormRepository::class.java)
    private val formHelper = mock(FormHelper::class.java)
    private val formSubmissionRepository = mock(FormSubmissionRepository::class.java)
    private val formService = FormService(formRepository, formHelper, formSubmissionRepository)

    private val form =
        Form(
            id = "1",
            classroomId = "class1",
            title = "Test Form 1",
            description = "Test form description",
            openDate = LocalDateTime.of(2021, 9, 1, 0, 0),
            closeDate = LocalDateTime.of(2021, 9, 30, 0, 0),
            fields =
                listOf(
                    FormField(
                        name = "field1",
                        type = "text",
                        required = true,
                        validation = null,
                    ),
                ),
        )

    @Test
    fun `createForm should insert and return form`() {
        `when`(formRepository.insert(form)).thenReturn(form)

        val result = formService.createForm(form)

        assertEquals(form, result)
        verify(formRepository, times(1)).insert(form)
    }

    @Test
    fun `getFormById should return form when found`() {
        `when`(formRepository.findById("1")).thenReturn(Optional.of(form))

        val result = formService.getFormById("1")

        assertEquals(form, result)
        verify(formRepository, times(1)).findById("1")
    }

    @Test
    fun `getFormById should throw exception when not found`() {
        `when`(formRepository.findById("1")).thenReturn(Optional.empty())

        val exception = assertThrows<Exception> { formService.getFormById("1") }

        assertEquals(ErrorMessages.FORM_NOT_FOUND, exception.message)
        verify(formRepository, times(1)).findById("1")
    }

    @Test
    fun `updateForm should update and return form`() {
        val updatedForm = form.copy(title = "Updated Form 1")
        `when`(formRepository.findById("1")).thenReturn(Optional.of(form))
        `when`(formRepository.save(form)).thenReturn(updatedForm)

        val result = formService.updateForm("1", updatedForm)

        assertEquals(updatedForm, result)
        verify(formRepository, times(1)).findById("1")
        verify(formRepository, times(1)).save(form)
    }

    @Test
    fun `findByClassroomId should return form when found`() {
        `when`(formRepository.findByClassroomId("class1")).thenReturn(form)

        val result = formService.findByClassroomId("class1")

        assertEquals(form, result)
        verify(formRepository, times(1)).findByClassroomId("class1")
    }

    @Test
    fun `findByClassroomId should throw exception when not found`() {
        `when`(formRepository.findByClassroomId("class1")).thenReturn(null)

        val exception = assertThrows<Exception> { formService.findByClassroomId("class1") }

        assertEquals(ErrorMessages.FORM_NOT_FOUND, exception.message)
        verify(formRepository, times(1)).findByClassroomId("class1")
    }

    @Test
    fun `deleteFormById should delete form by id`() {
        doNothing().`when`(formRepository).deleteById("1")

        formService.deleteFormById("1")

        verify(formRepository, times(1)).deleteById("1")
        verify(formSubmissionRepository, times(1)).deleteByFormId("1")
    }

    @Test
    fun `deleteFormSubmissionByFormId should delete form submission by form id`() {
        doNothing().`when`(formSubmissionRepository).deleteByFormId("1")

        formService.deleteFormSubmissionByFormId("1")

        verify(formSubmissionRepository, times(1)).deleteByFormId("1")
    }
}
