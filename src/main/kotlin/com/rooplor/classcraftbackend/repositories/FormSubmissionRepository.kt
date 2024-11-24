package com.rooplor.classcraftbackend.repositories

import com.rooplor.classcraftbackend.entities.FormSubmission
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface FormSubmissionRepository : MongoRepository<FormSubmission, String> {
    fun findByClassroomId(classroomId: String): List<FormSubmission>

    fun findByFormIdAndSubmittedBy(
        formId: String,
        submittedBy: String,
    ): FormSubmission?
}
