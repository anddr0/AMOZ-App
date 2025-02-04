package com.example.amoz.models

import com.example.amoz.api.enums.Sex
import com.example.amoz.api.serializers.LocalDateSerializer
import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Serializable
data class Person(
    @Serializable(with = UUIDSerializer::class)
    val personId: UUID,
    val name: String,
    val surname: String,
    @Serializable(with = LocalDateSerializer::class)
    val dateOfBirth: LocalDate,
    val sex: Sex
)
