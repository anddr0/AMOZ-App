package com.example.amoz.models

import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Invitation(
    val company: Company,
    val sender: Employee,
    @Serializable(with = UUIDSerializer::class)
    val token: UUID
)
