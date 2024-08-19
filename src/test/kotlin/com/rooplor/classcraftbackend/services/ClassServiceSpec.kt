package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.entities.Class
import com.rooplor.classcraftbackend.enums.Format
import com.rooplor.classcraftbackend.enums.Type
import com.rooplor.classcraftbackend.repositories.ClassRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import java.util.Optional

@SpringBootTest
class ClassServiceSpec {
    private val classRepository: ClassRepository = Mockito.mock(ClassRepository::class.java)
    private val classService: ClassService = ClassService(classRepository)

    @Test
    fun `should return all classes`() {
        val classes =
            listOf(
                Class(
                    title = "Test Class",
                    details = "Details",
                    target = "Target",
                    prerequisite = "None",
                    type = Type.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    date = listOf(),
                ),
                Class(
                    title = "Test Class 2",
                    details = "Details 2",
                    target = "Target 2",
                    prerequisite = "None",
                    type = Type.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    date = listOf(),
                ),
            )
        Mockito.`when`(classRepository.findAll()).thenReturn(classes)

        val result = classService.findAllClass()
        assertEquals(classes, result)
    }

    @Test
    fun `should return class by id`() {
        val classId = "1"
        val classObj =
            Class(
                id = classId,
                title = "Test Class",
                details = "Details",
                target = "Target",
                prerequisite = "None",
                type = Type.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                date = listOf(),
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classObj))

        val result = classService.findClassById(classId)
        assertEquals(classObj, result)
    }

    @Test
    fun `should insert a new class`() {
        val classObj =
            Class(
                title = "Test Class",
                details = "Details",
                target = "Target",
                prerequisite = "None",
                type = Type.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                date = listOf(),
            )
        Mockito.`when`(classRepository.insert(classObj)).thenReturn(classObj)

        val result = classService.insertClass(classObj)
        assertEquals(classObj, result)
    }
}
