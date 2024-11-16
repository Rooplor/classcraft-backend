package com.rooplor.classcraftbackend.repositories

import com.rooplor.classcraftbackend.entities.Form
import com.rooplor.classcraftbackend.entities.FormField
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito.`when`
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDateTime
import java.util.Optional
import kotlin.test.Test

@DataMongoTest
class FormRepositoryTest {
    @MockBean
    private lateinit var formRepository: FormRepository

    @Test
    fun `should save and find form by id`() {
        val formToSave =
            Form(
                id = "1",
                classroomId = "1",
                title = "Test Form",
                description = "Test form description",
                openDate = LocalDateTime.of(2021, 9, 1, 0, 0),
                closeDate = LocalDateTime.of(2021, 9, 30, 0, 0),
                fields =
                    listOf(
                        FormField(name = "email", type = "text", required = true, validation = null, options = null),
                        FormField(name = "phone", type = "text", required = false, validation = null, options = null),
                    ),
            )
        `when`(formRepository.save(formToSave)).thenReturn(formToSave)
        `when`(formRepository.findById(formToSave.id!!)).thenReturn(Optional.of(formToSave))

        val savedForm = formRepository.save(formToSave)
        val foundForm = formRepository.findById(savedForm.id!!).get()
        assertEquals(savedForm, foundForm)
    }

    @Test
    fun `should find all forms`() {
        val forms =
            listOf(
                Form(
                    id = "1",
                    classroomId = "1",
                    title = "Test Form 1",
                    description = "Test form description",
                    openDate = LocalDateTime.of(2021, 9, 1, 0, 0),
                    closeDate = LocalDateTime.of(2021, 9, 30, 0, 0),
                    fields =
                        listOf(
                            FormField(name = "email", type = "text", required = true, validation = null, options = null),
                            FormField(name = "phone", type = "text", required = false, validation = null, options = null),
                        ),
                ),
                Form(
                    id = "2",
                    classroomId = "1",
                    title = "Test Form 2",
                    description = "Test form description",
                    openDate = LocalDateTime.of(2021, 9, 1, 0, 0),
                    closeDate = LocalDateTime.of(2021, 9, 30, 0, 0),
                    fields =
                        listOf(
                            FormField(name = "address", type = "text", required = true, validation = null, options = null),
                            FormField(name = "city", type = "text", required = false, validation = null, options = null),
                        ),
                ),
            )
        `when`(formRepository.findAll()).thenReturn(forms)

        val foundForms = formRepository.findAll()
        assertEquals(forms, foundForms)
    }
}
