package com.example.amoz.models

import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.UUID

@Serializable
data class ContactPerson(
    @Serializable(with = UUIDSerializer::class)
    val contactPersonId: UUID,
    val contactNumber: String,
    val emailAddress: String?
)
