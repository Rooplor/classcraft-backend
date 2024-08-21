package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.dtos.VenueUpdateDTO
import com.rooplor.classcraftbackend.entities.Class
import com.rooplor.classcraftbackend.repositories.ClassRepository
import com.rooplor.classcraftbackend.utils.JsonValid.isValidJson
import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@AllArgsConstructor
@Service
class ClassService
    @Autowired
    constructor(
        private val classRepository: ClassRepository,
    ) {
        fun findAllClass(): List<Class> = classRepository.findAll()

        fun insertClass(addedClass: Class): Class {
            addedClass.registrationStatus = false
            addedClass.isPublished = false
            return classRepository.insert(addedClass)
        }

        fun findClassById(id: String): Class = classRepository.findById(id).orElseThrow()

        fun updateVenueClass(
            id: String,
            venueUpdateDTO: VenueUpdateDTO,
        ): Class {
            val classToUpdate = findClassById(id)
            classToUpdate.venue = venueUpdateDTO.venue
            classToUpdate.venueStatus = venueUpdateDTO.status
            return classRepository.save(classToUpdate)
        }

        fun updateMeetingUrlClass(
            id: String,
            meetingUrl: String,
        ): Class {
            val classToUpdate = findClassById(id)
            classToUpdate.meetingUrl = meetingUrl
            return classRepository.save(classToUpdate)
        }

        fun updateContent(
            id: String,
            classContent: String,
        ): Class {
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
        ): Class {
            val classToUpdate = findClassById(id)
            classToUpdate.registrationUrl = registrationUrl
            return classRepository.save(classToUpdate)
        }

        fun toggleRegistrationStatus(id: String): Class {
            val classToUpdate = findClassById(id)
            classToUpdate.registrationStatus = !classToUpdate.registrationStatus!!
            return classRepository.save(classToUpdate)
        }

        fun togglePublishStatus(id: String): Class {
            val classToUpdate = findClassById(id)
            classToUpdate.isPublished = !classToUpdate.isPublished!!
            return classRepository.save(classToUpdate)
        }
    }
