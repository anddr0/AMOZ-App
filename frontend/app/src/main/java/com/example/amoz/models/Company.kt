package com.example.amoz.models

import com.example.amoz.api.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.UUID

@Serializable
data class Company(
    @Serializable(with = UUIDSerializer::class)
    val companyId: UUID,
    val companyNumber: String,
    val name: String,
    val countryOfRegistration: String,
    val address: Address,
    val regon: String? = null
)