package com.example.amoz.models

import com.example.amoz.api.enums.RoleInCompany
import com.example.amoz.api.serializers.LocalDateSerializer
import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Serializable
data class Employee(
    @Serializable(with = UUIDSerializer::class)
    val employeeId: UUID,
    val user: User,
    val contactPerson: ContactPerson,
    val person: Person,
    val roleInCompany: RoleInCompany,
    @Serializable(with = LocalDateSerializer::class)
    val employmentDate: LocalDate
)
