package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.dtos.InitClassDTO
import com.rooplor.classcraftbackend.entities.Class
import com.rooplor.classcraftbackend.enums.Format
import com.rooplor.classcraftbackend.enums.Type
import com.rooplor.classcraftbackend.services.ClassService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ClassController::class)
class ClassControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var classService: ClassService

    @MockBean
    private lateinit var modelMapper: ModelMapper

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
        Mockito.`when`(classService.findAllClass()).thenReturn(classes)

        mockMvc
            .perform(get("/api/class"))
            .andExpect(status().isOk)
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
        Mockito.`when`(classService.findClassById(classId)).thenReturn(classObj)

        mockMvc
            .perform(get("/api/class/$classId"))
            .andExpect(status().isOk)
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
        val initClassDTO =
            InitClassDTO(
                title = "Test Class",
                details = "Details",
                target = "Target",
                prerequisite = "None",
                type = Type.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                date = listOf(),
            )
        Mockito.`when`(modelMapper.map(initClassDTO, Class::class.java)).thenReturn(classObj)
        Mockito.`when`(classService.insertClass(classObj)).thenReturn(classObj)

        mockMvc
            .perform(
                post("/api/class")
                    .contentType("application/json")
                    .content(
                        """
                        {
                            "title": "Test Class",
                            "details": "Details",
                            "target": "Target",
                            "prerequisite": "None",
                            "type": "LECTURE",
                            "format": "ONSITE",
                            "capacity": 30,
                            "date": []
                        }
                        """.trimIndent(),
                    ),
            ).andExpect(status().isOk)
    }
}
