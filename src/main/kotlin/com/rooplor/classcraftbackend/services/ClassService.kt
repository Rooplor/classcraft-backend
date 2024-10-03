package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.entities.Classroom
import com.rooplor.classcraftbackend.repositories.ClassroomRepository
import com.rooplor.classcraftbackend.utils.JsonValid.isValidJson
import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@AllArgsConstructor
@Service
class ClassService
    @Autowired
    constructor(
        private val classRepository: ClassroomRepository,
        private val venueService: VenueService,
    ) {
        fun findAllClass(registrationStatus: Boolean): List<Classroom> = classRepository.findByRegistrationStatusAndIsPublishedTrue(registrationStatus)

        fun insertClass(addedClassroom: Classroom): Classroom {
            addedClassroom.registrationStatus = false
            addedClassroom.isPublished = false
            return classRepository.insert(addedClassroom)
        }

        fun findClassById(id: String): Classroom = classRepository.findById(id).orElseThrow()

        fun updateVenueClass(
            id: String,
            venueId: String,
        ): Classroom {
            val classToUpdate = findClassById(id)
            classToUpdate.venue = venueService.findVenueById(venueId)
            return classRepository.save(classToUpdate)
        }

        fun updateMeetingUrlClass(
            id: String,
            meetingUrl: String,
        ): Classroom {
            val classToUpdate = findClassById(id)
            classToUpdate.meetingUrl = meetingUrl
            return classRepository.save(classToUpdate)
        }

        fun updateContent(
            id: String,
            classContent: String,
        ): Classroom {
            if (!isValidJson(classContent)) {
                throw IllegalArgumentException("Content is not a valid JSON")
            }
            val classToUpdate = findClassById(id)
            classToUpdate.content = classContent
            return classRepository.save(classToUpdate)
        }

        fun updateRegistrationUrl(
            id: String,
            registrationUrl: String,
        ): Classroom {
            val classToUpdate = findClassById(id)
            classToUpdate.registrationUrl = registrationUrl
            return classRepository.save(classToUpdate)
        }

        fun toggleRegistrationStatus(id: String): Classroom {
            val classToUpdate = findClassById(id)
            classToUpdate.registrationStatus = !classToUpdate.registrationStatus!!
            return classRepository.save(classToUpdate)
        }

        fun togglePublishStatus(id: String): Classroom {
            val classToUpdate = findClassById(id)
            classToUpdate.isPublished = !classToUpdate.isPublished!!
            return classRepository.save(classToUpdate)
        }

        fun deleteClass(id: String) {
            classRepository.deleteById(id)
        }
    }
