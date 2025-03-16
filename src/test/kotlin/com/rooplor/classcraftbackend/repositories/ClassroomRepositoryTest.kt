package com.rooplor.classcraftbackend.repositories

import com.rooplor.classcraftbackend.entities.Classroom
import com.rooplor.classcraftbackend.enums.ClassType
import com.rooplor.classcraftbackend.enums.Format
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Optional

@DataMongoTest
class ClassroomRepositoryTest {
    @MockBean
    private lateinit var classRepository: ClassroomRepository

    @Test
    fun `should save and find class by id`() {
        val classroomToSave =
            Classroom(
                id = "1",
                title = "React Native",
                details = "Learn how to build mobile apps with React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                dates = listOf(),
            )
        `when`(classRepository.save(classroomToSave)).thenReturn(classroomToSave)
        `when`(classRepository.findById(classroomToSave.id!!)).thenReturn(Optional.of(classroomToSave))

        val savedClass = classRepository.save(classroomToSave)
        val foundClass = classRepository.findById(savedClass.id!!).get()
        assertEquals(savedClass, foundClass)
    }

    @Test
    fun `should find all classes`() {
        val classrooms =
            listOf(
                Classroom(
                    id = "1",
                    title = "React Native",
                    details = "Learn how to build mobile apps with React Native",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    dates = listOf(),
                ),
                Classroom(
                    id = "2",
                    title = "Spring Boot 101",
                    details = "Learn how to build RESTful APIs with Spring Boot",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    dates = listOf(),
                ),
            )
        `when`(classRepository.saveAll(classrooms)).thenReturn(classrooms)
        `when`(classRepository.findAll()).thenReturn(classrooms)

        classRepository.saveAll(classrooms)
        val foundClasses = classRepository.findAll()
        val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        assertEquals(classrooms, foundClasses)
        assertEquals(
            foundClasses[0].createdWhen.format(timeFormatter),
            LocalDateTime.now().format(timeFormatter),
        )
    }

    @Test
    fun `should find all classes then filter published and registration`() {
        val classrooms =
            listOf(
                Classroom(
                    id = "1",
                    title = "React Native",
                    details = "Learn how to build mobile apps with React Native",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    dates = listOf(),
                    registrationStatus = true,
                    isPublished = true,
                ),
                Classroom(
                    id = "2",
                    title = "Spring Boot 101",
                    details = "Learn how to build RESTful APIs with Spring Boot",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    dates = listOf(),
                    registrationStatus = false,
                    isPublished = true,
                ),
            )
        `when`(classRepository.saveAll(classrooms)).thenReturn(classrooms)
        `when`(classRepository.findAll()).thenReturn(classrooms)
        `when`(classRepository.findByRegistrationStatusAndIsPublishedTrueOrderByCreatedWhen(true)).thenReturn(
            classrooms.filter {
                it.registrationStatus ==
                    true &&
                    it.isPublished == true
            },
        )

        classRepository.saveAll(classrooms)
        val foundClasses = classRepository.findAll()
        val filteredClasses = classRepository.findByRegistrationStatusAndIsPublishedTrueOrderByCreatedWhen(true)
        assertEquals(classrooms, foundClasses)
        assertEquals(classrooms.filter { it.registrationStatus == true && it.isPublished == true }, filteredClasses)
    }

    @Test
    fun `should delete class by id`() {
        val classroomToDelete =
            Classroom(
                id = "1",
                title = "React Native",
                details = "Learn how to build mobile apps with React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                dates = listOf(),
            )
        `when`(classRepository.save(classroomToDelete)).thenReturn(classroomToDelete)
        `when`(classRepository.findById(classroomToDelete.id!!)).thenReturn(Optional.of(classroomToDelete))
        `when`(classRepository.existsById(classroomToDelete.id!!)).thenReturn(true)

        val savedClass = classRepository.save(classroomToDelete)
        val foundClass = classRepository.findById(savedClass.id!!).get()
        classRepository.deleteById(foundClass.id!!)
        val isDeleted = classRepository.existsById(foundClass.id!!)
        assertEquals(true, isDeleted)
    }

    @Test
    fun `should update class`() {
        val classroomToUpdate =
            Classroom(
                id = "1",
                title = "React Native",
                details = "Learn how to build mobile apps with React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                dates = listOf(),
            )
        `when`(classRepository.save(classroomToUpdate)).thenReturn(classroomToUpdate)
        `when`(classRepository.findById(classroomToUpdate.id!!)).thenReturn(Optional.of(classroomToUpdate))

        val savedClass = classRepository.save(classroomToUpdate)
        val foundClass = classRepository.findById(savedClass.id!!).get()
        foundClass.title = "Updated Title"
        val updatedClass = classRepository.save(foundClass)
        assertEquals("Updated Title", updatedClass.title)
    }

    @Test
    fun `should return classes by owner`() {
        val classrooms =
            listOf(
                Classroom(
                    id = "1",
                    title = "React Native",
                    details = "Learn how to build mobile apps using React Native",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    dates = listOf(),
                    owner = "owner1",
                ),
                Classroom(
                    id = "2",
                    title = "Spring Boot 101",
                    details = "Learn how to build web apps using Spring Boot",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    dates = listOf(),
                    owner = "owner1",
                ),
            )

        `when`(classRepository.findByOwner("owner1")).thenReturn(classrooms)

        val result = classRepository.findByOwner("owner1")
        assertEquals(classrooms, result)
    }

    @Test
    fun `should return classes when search`() {
        val classrooms =
            listOf(
                Classroom(
                    id = "1",
                    title = "React Native",
                    details = "Learn how to build mobile apps using React Native",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    dates = listOf(),
                    isPublished = true,
                ),
                Classroom(
                    id = "2",
                    title = "Spring Boot 101",
                    details = "Learn how to build web apps using Spring Boot",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    dates = listOf(),
                    isPublished = true,
                ),
            )

        `when`(
            classRepository.findByIsPublishedTrueAndTitleContainingIgnoreCaseOrDetailsContainingIgnoreCaseAndIsPublishedTrue(
                "React",
                "React",
            ),
        ).thenReturn(classrooms)

        val result =
            classRepository.findByIsPublishedTrueAndTitleContainingIgnoreCaseOrDetailsContainingIgnoreCaseAndIsPublishedTrue(
                "React",
                "React",
            )
        assertEquals(classrooms, result)
    }
}
