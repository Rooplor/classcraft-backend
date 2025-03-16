package com.rooplor.classcraftbackend.helpers

import com.rooplor.classcraftbackend.entities.Classroom
import com.rooplor.classcraftbackend.types.DateDetail
import com.rooplor.classcraftbackend.types.DateWithVenue
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class ClassroomHelperTest {
    private val classroomHelper = ClassroomHelper()

    @Test
    fun `should validate classroom successfully`() {
        val classroom =
            Classroom(
                title = "Kotlin Basics",
                details = "Learn the basics of Kotlin",
                target = "Beginners",
                instructorName = "John Doe",
                instructorBio = "Experienced Kotlin developer",
                instructorAvatar = "avatar.png",
                instructorFamiliarity = "High",
                capacity = 30,
                dates =
                    listOf(
                        DateWithVenue(
                            date =
                                DateDetail(
                                    startDateTime = LocalDateTime.now(),
                                    endDateTime = LocalDateTime.now(),
                                ),
                            venueId = listOf("venue1", "venue2"),
                        ),
                    ),
            )

        assertDoesNotThrow { classroomHelper.validateClassroom(classroom) }
    }

    @Test
    fun `should throw exception for missing mandatory fields`() {
        val classroom =
            Classroom(
                title = "",
                details = "",
                target = "",
                instructorName = "",
                instructorBio = "",
                instructorAvatar = "",
                instructorFamiliarity = "",
                capacity = 0,
                dates = emptyList(),
            )

        val exception = assertThrows<Exception> { classroomHelper.validateClassroom(classroom) }
        assertEquals(
            "Title is mandatory, Details is mandatory, " +
                "Target is mandatory, Instructor name is mandatory, " +
                "Instructor bio is mandatory, Instructor avatar is mandatory, " +
                "Instructor familiarity is mandatory, Capacity must be greater than 0, Dates are mandatory",
            exception.message,
        )
    }
}
