package com.rooplor.classcraftbackend.repositories

import com.rooplor.classcraftbackend.entities.Class
import com.rooplor.classcraftbackend.enums.Format
import com.rooplor.classcraftbackend.enums.Type
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.Optional

@DataMongoTest
class ClassRepositoryTest {
    @MockBean
    private lateinit var classRepository: ClassRepository

    @Test
    fun `should save and find class by id`() {
        val classToSave =
            Class(
                id = "1",
                title = "Test Class",
                details = "Details",
                target = "Target",
                prerequisite = "None",
                type = Type.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                date = listOf(),
            )
        `when`(classRepository.save(classToSave)).thenReturn(classToSave)
        `when`(classRepository.findById(classToSave.id!!)).thenReturn(Optional.of(classToSave))

        val savedClass = classRepository.save(classToSave)
        val foundClass = classRepository.findById(savedClass.id!!).get()
        assertEquals(savedClass, foundClass)
    }

    @Test
    fun `should find all classes`() {
        val classes =
            listOf(
                Class(
                    id = "1",
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
                    id = "2",
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
        `when`(classRepository.saveAll(classes)).thenReturn(classes)
        `when`(classRepository.findAll()).thenReturn(classes)

        classRepository.saveAll(classes)
        val foundClasses = classRepository.findAll()
        assertEquals(classes, foundClasses)
    }
}
