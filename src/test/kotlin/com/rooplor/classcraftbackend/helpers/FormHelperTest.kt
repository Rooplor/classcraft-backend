package com.rooplor.classcraftbackend.helpers

import com.rooplor.classcraftbackend.entities.Form
import com.rooplor.classcraftbackend.entities.FormField
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.time.LocalDateTime

class FormHelperTest {
    private val formHelper = FormHelper()

    @Test
    fun `mergeListOfMaps should merge multiple maps into one`() {
        val list =
            listOf(
                mapOf("key1" to "value1"),
                mapOf("key2" to "value2"),
                mapOf("key3" to "value3"),
            )
        val result = formHelper.mergeListOfMaps(list)
        val expected = mapOf("key1" to "value1", "key2" to "value2", "key3" to "value3")
        assertEquals(expected, result)
    }

    @Test
    fun `validateFormField should return errors for invalid field`() {
        val field = FormField(name = "", type = "", required = false)
        try {
            formHelper.validateFormField(field)
        } catch (e: Exception) {
            assertEquals("Field name is mandatory, Field type is mandatory", e.message)
        }
    }

    @Test
    fun `validateFormField should return no errors for valid field`() {
        val field = FormField(name = "name", type = "type", required = true)
        assertDoesNotThrow { formHelper.validateFormField(field) }
    }

    @Test
    fun `validateForm should return errors for invalid form`() {
        val form =
            Form(
                classroomId = "",
                title = "",
                description = "",
                fields = emptyList(),
            )
        try {
            formHelper.validateForm(form)
        } catch (e: Exception) {
            assertEquals("Classroom ID is mandatory, Title is mandatory, Description is mandatory, Fields are mandatory", e.message)
        }
    }

    @Test
    fun `validateForm should return no errors for valid form`() {
        val field = FormField(name = "name", type = "type", required = true)
        val form =
            Form(
                classroomId = "class1",
                title = "title",
                description = "description",
                openDate = LocalDateTime.of(2021, 9, 1, 0, 0),
                closeDate = LocalDateTime.of(2021, 9, 30, 0, 0),
                fields = listOf(field),
            )
        assertDoesNotThrow { formHelper.validateForm(form) }
    }

    @Test
    fun `validateForm should return field errors for invalid fields`() {
        val field = FormField(name = "", type = "", required = false)
        val form =
            Form(
                classroomId = "class1",
                title = "title",
                description = "description",
                openDate = LocalDateTime.of(2021, 9, 1, 0, 0),
                closeDate = LocalDateTime.of(2021, 9, 30, 0, 0),
                fields = listOf(field),
            )
        try {
            formHelper.validateForm(form)
        } catch (e: Exception) {
            assertEquals("Field 0: Field name is mandatory, Field type is mandatory", e.message)
        }
    }
}
