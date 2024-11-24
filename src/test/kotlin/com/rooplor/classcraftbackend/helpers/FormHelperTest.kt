package com.rooplor.classcraftbackend.helpers

import com.rooplor.classcraftbackend.dtos.FormCreateDTO
import com.rooplor.classcraftbackend.entities.FormField
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
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
        val result = formHelper.validateFormField(field)
        val expected = listOf("Field name is mandatory", "Field type is mandatory")
        assertEquals(expected, result)
    }

    @Test
    fun `validateFormField should return no errors for valid field`() {
        val field = FormField(name = "name", type = "type", required = true)
        val result = formHelper.validateFormField(field)
        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun `validateForm should return errors for invalid form`() {
        val form =
            FormCreateDTO(
                classroomId = "",
                title = "",
                description = "",
                fields = emptyList(),
            )
        val result = formHelper.validateForm(form)
        val expected =
            listOf(
                "Classroom ID is mandatory",
                "Title is mandatory",
                "Description is mandatory",
                "Fields are mandatory",
            )
        assertEquals(expected, result)
    }

    @Test
    fun `validateForm should return no errors for valid form`() {
        val field = FormField(name = "name", type = "type", required = true)
        val form =
            FormCreateDTO(
                classroomId = "class1",
                title = "title",
                description = "description",
                openDate = LocalDateTime.of(2021, 9, 1, 0, 0),
                closeDate = LocalDateTime.of(2021, 9, 30, 0, 0),
                fields = listOf(field),
            )
        val result = formHelper.validateForm(form)
        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun `validateForm should return field errors for invalid fields`() {
        val field = FormField(name = "", type = "", required = false)
        val form =
            FormCreateDTO(
                classroomId = "class1",
                title = "title",
                description = "description",
                openDate = LocalDateTime.of(2021, 9, 1, 0, 0),
                closeDate = LocalDateTime.of(2021, 9, 30, 0, 0),
                fields = listOf(field),
            )
        val result = formHelper.validateForm(form)
        val expected = listOf("Field 0: Field name is mandatory, Field type is mandatory")
        assertEquals(expected, result)
    }
}
