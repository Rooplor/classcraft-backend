package com.rooplor.classcraftbackend.entities

import lombok.Data
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.ZonedDateTime

@Data
@Document
class Student(
    @Id
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val gender: Gender,
    val address: Address,
    val favouriteSubjects: List<String>,
    val totalSpentInBooks: BigDecimal,
    val created: ZonedDateTime,
)
