package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.entities.Classroom
import com.rooplor.classcraftbackend.enums.Status
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.repositories.ClassroomRepository
import com.rooplor.classcraftbackend.utils.JsonValid.isValidJson
import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@AllArgsConstructor
@Service
class ClassService
    @Autowired
    constructor(
        private val classRepository: ClassroomRepository,
        private val venueService: VenueService,
        private val authService: AuthService,
        private val userService: UserService,
    ) {
        fun findAllClassPublished(registrationStatus: Boolean): List<Classroom> =
            classRepository.findByRegistrationStatusAndIsPublishedTrueOrderByCreatedWhen(registrationStatus)

        fun insertClass(addedClassroom: Classroom): Classroom {
            addedClassroom.registrationStatus = false
            addedClassroom.isPublished = false
            addedClassroom.owner =
                authService.getAuthenticatedUserDetails()?.id ?: throw Exception(ErrorMessages.USER_NOT_FOUND)
            return classRepository.insert(addedClassroom)
        }

        fun updateClass(
            id: String,
            updatedClassroom: Classroom,
        ): Classroom {
            val classToUpdate = findClassById(id)
            classToUpdate.title = updatedClassroom.title
            classToUpdate.details = updatedClassroom.details
            classToUpdate.target = updatedClassroom.target
            classToUpdate.prerequisite = updatedClassroom.prerequisite
            classToUpdate.type = updatedClassroom.type
            classToUpdate.format = updatedClassroom.format
            classToUpdate.capacity = updatedClassroom.capacity
            classToUpdate.date = updatedClassroom.date
            if (updatedClassroom.coOwners != null) {
                validateCoOwners(updatedClassroom.coOwners!!)
                classToUpdate.coOwners = updatedClassroom.coOwners
            }
            classToUpdate.coOwners = updatedClassroom.coOwners
            return classRepository.save(updateUpdatedWhen(classToUpdate))
        }

        fun findClassById(id: String): Classroom = classRepository.findById(id).orElseThrow()

        fun updateVenueClass(
            id: String,
            venueId: String,
        ): Classroom {
            val classToUpdate = findClassById(id)
            classToUpdate.venue = venueService.findVenueById(venueId)
            return classRepository.save(updateUpdatedWhen(classToUpdate))
        }

        fun updateMeetingUrlClass(
            id: String,
            meetingUrl: String,
        ): Classroom {
            val classToUpdate = findClassById(id)
            classToUpdate.meetingUrl = meetingUrl
            return classRepository.save(updateUpdatedWhen(classToUpdate))
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
            return classRepository.save(updateUpdatedWhen(classToUpdate))
        }

        fun updateRegistrationUrl(
            id: String,
            registrationUrl: String,
        ): Classroom {
            val classToUpdate = findClassById(id)
            classToUpdate.registrationUrl = registrationUrl
            return classRepository.save(updateUpdatedWhen(classToUpdate))
        }

        fun toggleRegistrationStatus(id: String): Classroom {
            val classToUpdate = findClassById(id)
            classToUpdate.registrationStatus = !classToUpdate.registrationStatus!!
            return classRepository.save(updateUpdatedWhen(classToUpdate))
        }

        fun togglePublishStatus(id: String): Classroom {
            val classToUpdate = findClassById(id)
            classToUpdate.isPublished = !classToUpdate.isPublished!!
            return classRepository.save(updateUpdatedWhen(classToUpdate))
        }

        fun deleteClass(id: String) {
            classRepository.deleteById(id)
        }

        fun findClassByOwners(owners: List<String>): List<Classroom> = owners.flatMap { owner -> classRepository.findByOwner(owner) }

        fun updateStepperStatus(id: String): Classroom {
            val classToUpdate = findClassById(id)
            when (classToUpdate.stepperStatus) {
                Status.FILL_CLASS_DETAIL -> classToUpdate.stepperStatus = Status.RESERVE_VENUE
                Status.RESERVE_VENUE -> classToUpdate.stepperStatus = Status.CRAFT_CONTENT
                Status.CRAFT_CONTENT -> classToUpdate.stepperStatus = Status.PREPARE_FOR_REG
                else -> throw Exception("Invalid stepper status or this class is already completed")
            }
            return classRepository.save(updateUpdatedWhen(classToUpdate))
        }

        private fun updateUpdatedWhen(classroom: Classroom): Classroom {
            classroom.updatedWhen = LocalDateTime.now()
            return classroom
        }

        private fun validateCoOwners(coOwners: List<String>) {
            val errorList = mutableListOf<String>()
            coOwners.forEach {
                if (!userService.isUserExistById(it)) {
                    errorList.add(ErrorMessages.USER_WITH_ID_DOSE_NOT_EXIST.replace("\$0", it))
                }
            }
            if (errorList.isNotEmpty()) {
                throw Exception(errorList.joinToString(", "))
            }
        }
    }
