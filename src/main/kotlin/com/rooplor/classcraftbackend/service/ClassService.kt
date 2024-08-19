package com.rooplor.classcraftbackend.service

import com.rooplor.classcraftbackend.entities.Class
import com.rooplor.classcraftbackend.repositories.ClassRepository
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

        fun insertClass(addedClass: Class) = classRepository.insert(addedClass)
    }
